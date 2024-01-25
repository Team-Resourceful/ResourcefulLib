package com.teamresourceful.resourcefullib.common.module.forge;

import com.teamresourceful.resourcefullib.common.modules.Module;
import com.teamresourceful.resourcefullib.common.modules.ModuleType;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModuleCapabilityProvider<M extends Module<M>> implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private final M module;
    private final Capability<M> capability;
    private final LazyOptional<M> optional;

    public ModuleCapabilityProvider(ModuleRegistryEntry<M, ModuleType<M>> entry) {
        this.module = entry.get().create();
        this.capability = entry.capability();
        this.optional = LazyOptional.of(() -> module);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        return this.capability.orEmpty(capability, optional);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        module.save(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag arg) {
        module.read(arg);
    }
}
