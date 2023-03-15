package com.teamresourceful.resourcefullib.common.codecs.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;

public final class ItemStackCodec {

    private static final Codec<ItemStack> STRING_EITHER = BuiltInRegistries.ITEM.byNameCodec().xmap(ItemStack::new, ItemStack::getItem);

    private static final Codec<ItemStack> STACK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("id").forGetter(ItemStack::getItem),
            Codec.INT.fieldOf("count").orElse(1).forGetter(ItemStack::getCount),
            CompoundTag.CODEC.optionalFieldOf("nbt").forGetter(o -> Optional.ofNullable(o.getTag()))
    ).apply(instance, ItemStackCodec::createItemStack));

    public static final Codec<ItemStack> CODEC = CodecExtras.eitherRight(Codec.either(STRING_EITHER, STACK_CODEC));

    public static final Codec<ItemStack> NETWORK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecExtras.registryId(BuiltInRegistries.ITEM).fieldOf("id").forGetter(ItemStack::getItem),
            Codec.INT.fieldOf("count").orElse(1).forGetter(ItemStack::getCount),
            CompoundTag.CODEC.optionalFieldOf("nbt").forGetter(o -> Optional.ofNullable(o.getTag()))
    ).apply(instance, ItemStackCodec::createItemStack));

    private ItemStackCodec() throws UtilityClassException {
        throw new UtilityClassException();
    }

    private static ItemStack createItemStack(ItemLike item, int count, Optional<CompoundTag> tagOptional) {
        ItemStack stack = new ItemStack(item, count);
        tagOptional.ifPresent(stack::setTag);
        return stack;
    }
}
