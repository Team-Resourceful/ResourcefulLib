package com.teamresourceful.resourcefullib.common.codecs.recipes;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public final class ItemStackCodec {

    private static final Codec<ItemStack> STRING_EITHER = BuiltInRegistries.ITEM.byNameCodec().xmap(ItemStack::new, ItemStack::getItem);

    private static final Codec<ItemStack> STACK_CODEC = ItemStack.CODEC;

    public static final Codec<ItemStack> CODEC = CodecExtras.eitherRight(Codec.either(STRING_EITHER, STACK_CODEC));

    private ItemStackCodec() throws UtilityClassException {
        throw new UtilityClassException();
    }

    private static ItemStack createItemStack(ItemLike item, int count, DataComponentPatch components) {
        return new ItemStack(item.asItem().builtInRegistryHolder(), count, components);
    }
}
