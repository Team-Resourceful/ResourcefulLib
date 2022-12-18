package com.teamresourceful.resourcefullib.common.codecs.recipes;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import com.teamresourceful.resourcefullib.common.item.LazyHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public final class LazyHolders {

    public static final Codec<LazyHolder<Item>> LAZY_ITEM = ResourceLocation.CODEC.xmap(LazyHolder.map(BuiltInRegistries.ITEM), LazyHolder::getId);
    public static final Codec<LazyHolder<Block>> LAZY_BLOCK = ResourceLocation.CODEC.xmap(LazyHolder.map(BuiltInRegistries.BLOCK), LazyHolder::getId);
    public static final Codec<LazyHolder<Fluid>> LAZY_FLUID = ResourceLocation.CODEC.xmap(LazyHolder.map(BuiltInRegistries.FLUID), LazyHolder::getId);
    public static final Codec<LazyHolder<EntityType<?>>> LAZY_ENTITY = ResourceLocation.CODEC.xmap(LazyHolder.map(BuiltInRegistries.ENTITY_TYPE), LazyHolder::getId);
    public static final Codec<LazyHolder<MobEffect>> LAZY_EFFECT = ResourceLocation.CODEC.xmap(LazyHolder.map(BuiltInRegistries.MOB_EFFECT), LazyHolder::getId);
    public static final Codec<LazyHolder<SoundEvent>> LAZY_SOUND = ResourceLocation.CODEC.xmap(LazyHolder.map(BuiltInRegistries.SOUND_EVENT), LazyHolder::getId);

    private LazyHolders() throws UtilityClassException {
        throw new UtilityClassException();
    }

}
