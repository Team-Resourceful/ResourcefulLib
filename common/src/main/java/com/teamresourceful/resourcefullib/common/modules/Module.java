package com.teamresourceful.resourcefullib.common.modules;

import net.minecraft.nbt.CompoundTag;

public interface Module<T extends Module<T>> {

    void read(CompoundTag tag);

    void save(CompoundTag tag);

}
