package com.teamresourceful.resourcefullib.mixins.fabric;

import com.mojang.datafixers.DataFixer;
import com.teamresourceful.resourcefullib.fabric.FabricServerProxy;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.Proxy;

@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin extends MinecraftServer {

    public DedicatedServerMixin(Thread thread, LevelStorageSource.LevelStorageAccess levelStorageAccess, PackRepository packRepository, WorldStem worldStem, Proxy proxy, DataFixer dataFixer, Services services, ChunkProgressListenerFactory chunkProgressListenerFactory) {
        super(thread, levelStorageAccess, packRepository, worldStem, proxy, dataFixer, services, chunkProgressListenerFactory);
    }

    @Inject(method = "initServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/GameProfileCache;setUsesAuthentication(Z)V",
                    shift = At.Shift.AFTER
            )
    )
    private void onServerStarting(CallbackInfoReturnable<Boolean> cir) {
        FabricServerProxy.server = this;
    }
}
