package com.teamresourceful.resourcefullib.mixins.fabric;

import com.teamresourceful.resourcefullib.fabric.FabricServerProxy;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(
            method = "runServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;onServerExit()V",
                    shift = At.Shift.BEFORE
            )
    )
    private void onServerExit(CallbackInfo ci) {
        FabricServerProxy.server = null;
    }
}
