package com.teamresourceful.resourcefullib.common.recipe.ingredient.forge;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredient;
import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredientSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import org.jetbrains.annotations.NotNull;

public class ForgeIngredientSerializer<T extends CodecIngredient<T>> implements IIngredientSerializer<ForgeIngredient<T>> {

    private final CodecIngredientSerializer<T> serializer;
    private final Codec<ForgeIngredient<T>> codec;

    public ForgeIngredientSerializer(CodecIngredientSerializer<T> serializer) {
        this.serializer = serializer;
        this.codec = this.serializer.codec().xmap(ForgeIngredient::new, ForgeIngredient::getIngredient);
    }

    public ResourceLocation id() {
        return serializer.id();
    }

    @Override
    public Codec<? extends ForgeIngredient<T>> codec() {
        return codec;
    }

    @Override
    public ForgeIngredient<T> read(FriendlyByteBuf buf) {
        return new ForgeIngredient<>(serializer.network().decode(buf));
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buf, @NotNull ForgeIngredient<T> ingredient) {
        serializer.network().encode(ingredient.getIngredient(), buf);
    }
}
