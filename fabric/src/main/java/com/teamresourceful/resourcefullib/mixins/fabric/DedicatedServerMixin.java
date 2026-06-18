package com.teamresourceful.resourcefullib.mixins.fabric;

import com.mojang.datafixers.DataFixer;
import com.teamresourceful.resourcefullib.fabric.FabricServerProxy;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerSettings;
import net.minecraft.server.jsonrpc.ManagementServer;
import net.minecraft.server.level.progress.LevelLoadListener;
import net.minecraft.server.level.progress.LoggingLevelLoadListener;
import net.minecraft.server.notifications.NotificationManager;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.Proxy;
import java.util.Optional;

@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin extends MinecraftServer {

    public DedicatedServerMixin(Thread serverThread, LevelStorageSource.LevelStorageAccess levelStorageSource, PackRepository packRepository, WorldStem worldStem, final Optional<GameRules> gameRules, DedicatedServerSettings settings, DataFixer fixerUpper, Services services, @Nullable ManagementServer jsonRpcServer, NotificationManager notificationManager) {
        super(serverThread, levelStorageSource, packRepository, worldStem, gameRules, Proxy.NO_PROXY, fixerUpper, services, LoggingLevelLoadListener.forDedicatedServer(), true, notificationManager);
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
