package com.teamresourceful.resourcefullib.common.modules.fabric;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.modules.Module;
import com.teamresourceful.resourcefullib.common.modules.ModuleType;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntries;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class ModuleRegistry implements ResourcefulRegistry<ModuleType<?>> {

    private final RegistryEntries<ModuleType<?>> entries = new RegistryEntries<>();
    private final String modid;

    public ModuleRegistry(String id) {
        this.modid = id;
    }

    @Override
    public <I extends ModuleType<?>> RegistryEntry<I> register(String id, Supplier<I> supplier) {
        ModuleType<?> type = supplier.get();
        return this.entries.add(new ModuleRegistryEntry<>(
                register(type, new ResourceLocation(modid, id)),
                supplier
        ));
    }

    private static <T extends Module> AttachmentType<T> register(ModuleType<T> type, ResourceLocation id) {
        AttachmentRegistry.Builder<T> builder = AttachmentRegistry.builder();
        builder.initializer(type::create);
        Codec<T> moduleCodec = CompoundTag.CODEC.xmap(tag -> {
            T module = type.create();
            module.read(tag);
            return module;
        }, module -> {
            CompoundTag tag = new CompoundTag();
            module.save(tag);
            return tag;
        });

        builder.persistent(moduleCodec);
        if (type.copy()) builder.copyOnDeath();
        return builder.buildAndRegister(id);
    }

    @Override
    public Collection<RegistryEntry<ModuleType<?>>> getEntries() {
        return this.entries.getEntries();
    }

    @Override
    public void init() {

    }
}
