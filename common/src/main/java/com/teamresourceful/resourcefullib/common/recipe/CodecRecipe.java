package com.teamresourceful.resourcefullib.common.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

public interface CodecRecipe<C extends Container> extends Recipe<C> {

    @NotNull
    ResourceLocation id();

    @Override
    @NotNull
    default ResourceLocation getId() {
        return id();
    }

    @Override
    default boolean isSpecial() {
        return true;
    }

    @Override
    @NotNull
    default ItemStack assemble(@NotNull C pContainer) {
        return ItemStack.EMPTY;
    }

    @Override
    @NotNull
    default ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    default boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    /**
     * Gets the codec used to serialize this recipe to json.
     */
    default <T extends CodecRecipe<C>> Codec<T> jsonCodec(ResourceLocation id) {
        throw new UnsupportedOperationException("Serialization codec not implemented for " + id);
    }

    /**
     * Gets the codec used to serialize this recipe to network.
     */
    default <T extends CodecRecipe<C>> Codec<T> networkCodec(ResourceLocation id) {
        return jsonCodec(id);
    }
}

