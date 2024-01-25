package com.teamresourceful.resourcefullib.common.module.forge;

import com.teamresourceful.resourcefullib.common.modules.Module;
import com.teamresourceful.resourcefullib.common.modules.ModuleTarget;
import com.teamresourceful.resourcefullib.common.modules.ModuleType;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntries;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModuleRegistry implements ResourcefulRegistry<ModuleType<?>> {

    private final String id;
    private final RegistryEntries<ModuleType<?>> entries = new RegistryEntries<>();
    private final RegistryEntries<ModuleType<?>> copyEntries = new RegistryEntries<>();

    public ModuleRegistry(String id) {
        this.id = id;
    }

    @Override
    public <I extends ModuleType<?>> RegistryEntry<I> register(String id, Supplier<I> supplier) {
        RegistryEntry<I> entry = new ModuleRegistryEntry<>(new ResourceLocation(this.id, id), supplier);
        if (supplier.get().copy()) {
            return this.copyEntries.add(entry);
        }
        return this.entries.add(entry);
    }

    @Override
    public Collection<RegistryEntry<ModuleType<?>>> getEntries() {
        return this.entries.getEntries();
    }

    @Override
    public void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterCapabilities);
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, this::onEntityAttach);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerClone);
    }

    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        forEach(this.entries, entry -> HackyCapabilityManager.register(entry.get().type()));
    }

    public void onEntityAttach(AttachCapabilitiesEvent<Entity> event) {
        forEach(this.entries, entry -> {
            boolean isPlayer = event.getObject() instanceof ServerPlayer;
            boolean isLiving = event.getObject() instanceof LivingEntity && !isPlayer;
            if (isLiving && entry.get().targets().contains(ModuleTarget.LIVING)) {
                event.addCapability(entry.getId(), new ModuleCapabilityProvider<>(entry));
            } else if (isPlayer && entry.get().targets().contains(ModuleTarget.PLAYER)) {
                event.addCapability(entry.getId(), new ModuleCapabilityProvider<>(entry));
            }
        });
    }

    public void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getOriginal() instanceof ServerPlayer oldPlayer) {
            oldPlayer.reviveCaps();
            forEach(this.copyEntries, entry -> copyData(entry.capability(), oldPlayer, player, event.isWasDeath()));
            oldPlayer.invalidateCaps();
        }
    }

    private static <T extends Module<T>> void copyData(Capability<T> cap, ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean loseless) {
        if (!loseless) return;
        oldPlayer.getCapability(cap).ifPresent(oldCap -> newPlayer.getCapability(cap).ifPresent(newCap -> {
            CompoundTag data = new CompoundTag();
            oldCap.save(data);
            newCap.read(data);
        }));
    }

    @SuppressWarnings("unchecked")
    private static  <M extends Module<M>, T extends ModuleType<M>> void forEach(RegistryEntries<ModuleType<?>> entries, Consumer<ModuleRegistryEntry<M, T>> consumer) {
        for (RegistryEntry<ModuleType<?>> entry : entries.getEntries()) {
            if (!(entry instanceof ModuleRegistryEntry<?, ?> moduleEntry)) continue;
            consumer.accept((ModuleRegistryEntry<M, T>) moduleEntry);
        }
    }
}
