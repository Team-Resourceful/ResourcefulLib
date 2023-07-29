package com.teamresourceful.resourcefullib.common.bytecodecs;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

public class ItemStackByteCodec {

    public static final ByteCodec<ItemStack> EMPTY_ITEM = ByteCodec.unit(ItemStack.EMPTY);
    public static final ByteCodec<ItemStack> SINGLE_ITEM = ExtraByteCodecs.ITEM.map(ItemStack::new, ItemStack::getItem);
    public static final ByteCodec<ItemStack> ITEM_WITH_COUNT = ObjectByteCodec.create(
            ExtraByteCodecs.ITEM.fieldOf(ItemStack::getItem),
            ByteCodec.INT.fieldOf(ItemStack::getCount),
            ItemStack::new
    );
    public static final ByteCodec<ItemStack> ITEM_WITH_COUNT_AND_TAG = ObjectByteCodec.create(
            new IdMapByteCodec<>(BuiltInRegistries.ITEM).fieldOf(ItemStack::getItem),
            ByteCodec.INT.fieldOf(ItemStack::getCount),
            ExtraByteCodecs.COMPOUND_TAG.fieldOf(CodecExtras.optionalFor(ItemStack::getTag)),
            (item, count, tag) -> {
                ItemStack stack = new ItemStack(item, count);
                tag.ifPresent(stack::setTag);
                return stack;
            }
    );

    public static final ByteCodec<ItemStack> CODEC = ByteCodec.BYTE.dispatch((id) -> switch (id) {
        case 1 -> SINGLE_ITEM;
        case 2 -> ITEM_WITH_COUNT;
        case 3 -> ITEM_WITH_COUNT_AND_TAG;
        default -> EMPTY_ITEM;
    }, stack -> (byte)(stack.hasTag() ? 3 : stack.getCount() > 1 ? 2 : !stack.isEmpty() ? 1 : 0));
}
