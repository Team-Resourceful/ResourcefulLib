package com.teamresourceful.resourcefullib.common.modules;

import net.minecraft.resources.ResourceLocation;

import java.util.EnumSet;

public interface ModuleType<T extends Module<T>> {

    ResourceLocation id();

    Class<T> type();

    T create();

    boolean copy();

    EnumSet<ModuleTarget> targets();
}
