package com.teamresourceful.resourcefullib.mixins.fabric;

import com.mojang.datafixers.DataFixer;
import com.teamresourceful.resourcefullib.fabric.FabricServerProxy;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.progress.LevelLoadListener;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.Proxy;
import java.util.Optional;

@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin extends MinecraftServer {

    public DedicatedServerMixin(Thread serverThread, LevelStorageSource.LevelStorageAccess storageSource, PackRepository packRepository, WorldStem worldStem, Optional<GameRules> gameRules, Proxy proxy, DataFixer fixerUpper, Services services, LevelLoadListener levelLoadListener, boolean propagatesCrashes) {
        super(serverThread, storageSource, packRepository, worldStem, gameRules, proxy, fixerUpper, services, levelLoadListener, propagatesCrashes);
    }

    @Inject(method = "initServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/UserNameToIdResolver;resolveOfflineUsers(Z)V",
                    shift = At.Shift.AFTER
            )
    )
    private void onServerStarting(CallbackInfoReturnable<Boolean> cir) {
        FabricServerProxy.server = this;
    }
}
