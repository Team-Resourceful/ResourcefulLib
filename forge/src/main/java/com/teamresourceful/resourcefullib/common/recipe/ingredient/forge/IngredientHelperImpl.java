package com.teamresourceful.resourcefullib.common.recipe.ingredient.forge;

import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredient;
import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredientSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class IngredientHelperImpl {

    private static final Map<String, DeferredRegister<IIngredientSerializer<?>>> REGISTRIES = new HashMap<>();

    public static DeferredRegister<IIngredientSerializer<?>> getOrCreate(String namespace) {
        return REGISTRIES.computeIfAbsent(namespace, s -> {
            var registry = DeferredRegister.create(ForgeRegistries.INGREDIENT_SERIALIZERS.get(), s);
            registry.register(FMLJavaModLoadingContext.get().getModEventBus());
            return registry;
        });
    }

    public static <T extends CodecIngredient<T>> Ingredient getIngredient(T ingredient) {
        return new ForgeIngredient<>(ingredient);
    }

    public static <C extends CodecIngredient<C>, T extends CodecIngredientSerializer<C>> void registerIngredient(T serializer) {
        DeferredRegister<IIngredientSerializer<?>> registry = getOrCreate(serializer.id().getNamespace());
        registry.register(serializer.id().getPath(), () -> new ForgeIngredientSerializer<>(serializer));
    }
}
