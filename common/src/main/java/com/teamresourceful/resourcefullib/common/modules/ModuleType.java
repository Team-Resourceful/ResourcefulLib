package com.teamresourceful.resourcefullib.common.modules;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public interface ModuleType<T extends Module> {

    Class<T> type();

    T create();

    boolean copy();

    EnumSet<ModuleTarget> targets();
}
