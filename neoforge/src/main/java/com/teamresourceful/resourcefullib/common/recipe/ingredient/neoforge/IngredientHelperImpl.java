package com.teamresourceful.resourcefullib.common.recipe.ingredient.neoforge;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredient;
import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredientSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class IngredientHelperImpl {

    private static final Map<String, DeferredRegister<IngredientType<?>>> REGISTRIES = new HashMap<>();

    private static DeferredRegister<IngredientType<?>> getOrCreate(String namespace) {
        return REGISTRIES.computeIfAbsent(namespace, s -> {
            var registry = DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, s);
            registry.register(FMLJavaModLoadingContext.get().getModEventBus());
            return registry;
        });
    }

    public static <T extends CodecIngredient<T>> Ingredient getIngredient(T ingredient) {
        return new NeoForgeIngredient<>(ingredient);
    }

    public static <C extends CodecIngredient<C>, T extends CodecIngredientSerializer<C>> void registerIngredient(T serializer) {
        Codec<NeoForgeIngredient<C>> codec = serializer.codec().xmap(NeoForgeIngredient::new, NeoForgeIngredient::getIngredient);
        getOrCreate(serializer.id().getNamespace()).register(serializer.id().getPath(), () -> new IngredientType<>(codec));
    }
}
