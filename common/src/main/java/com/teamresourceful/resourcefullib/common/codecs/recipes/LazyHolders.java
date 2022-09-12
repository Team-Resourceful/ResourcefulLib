package com.teamresourceful.resourcefullib.common.codecs.recipes;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.item.LazyHolder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public final class LazyHolders {

    public static final Codec<LazyHolder<Item>> LAZY_ITEM = ResourceLocation.CODEC.xmap(LazyHolder.map(Registry.ITEM), LazyHolder::getId);
    public static final Codec<LazyHolder<Block>> LAZY_BLOCK = ResourceLocation.CODEC.xmap(LazyHolder.map(Registry.BLOCK), LazyHolder::getId);
    public static final Codec<LazyHolder<Fluid>> LAZY_FLUID = ResourceLocation.CODEC.xmap(LazyHolder.map(Registry.FLUID), LazyHolder::getId);

}
