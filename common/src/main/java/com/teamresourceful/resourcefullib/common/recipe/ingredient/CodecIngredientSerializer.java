package com.teamresourceful.resourcefullib.common.recipe.ingredient;

import com.mojang.serialization.Codec;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import net.minecraft.resources.ResourceLocation;

public record CodecIngredientSerializer<T extends CodecIngredient<T>>(ResourceLocation id, Codec<T> codec, ByteCodec<T> network) {

}
