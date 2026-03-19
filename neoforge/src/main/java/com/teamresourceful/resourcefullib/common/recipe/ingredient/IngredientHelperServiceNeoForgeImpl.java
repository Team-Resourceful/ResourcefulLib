package com.teamresourceful.resourcefullib.common.recipe.ingredient;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.Map;

class IngredientHelperServiceNeoForgeImpl implements IngredientHelperService {

    private static final Map<String, DeferredRegister<IngredientType<?>>> REGISTRIES = new HashMap<>();

    private static DeferredRegister<IngredientType<?>> getOrCreate(String namespace) {
        return REGISTRIES.computeIfAbsent(namespace, s -> {
            var registry = DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, s);
            registry.register(ModLoadingContext.get().getActiveContainer().getEventBus());
            return registry;
        });
    }

    @Override
    public <T extends CodecIngredient<T>> Ingredient get(T ingredient) {
        return new NeoForgeIngredient<>(ingredient).toVanilla();
    }

    @Override
    public <C extends CodecIngredient<C>, T extends CodecIngredientSerializer<C>> void register(T serializer) {
        MapCodec<NeoForgeIngredient<C>> codec = serializer.codec().xmap(NeoForgeIngredient::new, NeoForgeIngredient::ingredient);
        StreamCodec<RegistryFriendlyByteBuf, NeoForgeIngredient<C>> network = serializer.network().map(NeoForgeIngredient::new, NeoForgeIngredient::ingredient);
        getOrCreate(serializer.id().getNamespace()).register(serializer.id().getPath(), () -> new IngredientType<>(codec, network));
    }
}
