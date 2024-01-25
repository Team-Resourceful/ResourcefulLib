package com.teamresourceful.resourcefullib.common.module.neoforge;

import com.teamresourceful.resourcefullib.common.modules.Module;
import com.teamresourceful.resourcefullib.common.modules.ModuleType;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntries;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.teamresourceful.resourcefullib.neoforge.ResourcefulLibNeoForge;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Collection;
import java.util.function.Supplier;

public class ModuleRegistry implements ResourcefulRegistry<ModuleType<?>> {

    private final DeferredRegister<AttachmentType<?>> registry;
    private final RegistryEntries<ModuleType<?>> entries = new RegistryEntries<>();

    public ModuleRegistry(String id) {
        this.registry = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, id);
    }

    @Override
    public <I extends ModuleType<?>> RegistryEntry<I> register(String id, Supplier<I> supplier) {
        return this.entries.add(new ModuleRegistryEntry<>(registry.register(id, () -> {
            ModuleType<?> type = supplier.get();
            return builder(type);
        }), supplier));
    }

    private static <T extends Module<T>> AttachmentType<T> builder(ModuleType<T> type) {
        AttachmentType.Builder<T> builder = AttachmentType
                .builder(type::create)
                .serialize(new ModuleSerializer<>(type));
        if (type.copy()) {
            builder.copyOnDeath();
        }

        return builder.build();
    }

    @Override
    public Collection<RegistryEntry<ModuleType<?>>> getEntries() {
        return this.entries.getEntries();
    }

    @Override
    public void init() {
        registry.register(ResourcefulLibNeoForge.getModEventBus());
    }
}
