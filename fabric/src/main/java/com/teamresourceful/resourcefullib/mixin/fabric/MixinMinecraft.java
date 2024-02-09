package com.teamresourceful.resourcefullib.mixin.fabric;

import com.mojang.realmsclient.client.RealmsClient;
import com.teamresourceful.resourcefullib.client.compatibility.CompatibilityWarningScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.resources.ReloadInstance;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow public abstract void setScreen(@Nullable Screen screen);

    @Inject(method = "setInitialScreen", at = @At("HEAD"), cancellable = true)
    public void resourcefullib$setInitialScreen(RealmsClient realmsClient, ReloadInstance reloadInstance, GameConfig.QuickPlayData quickPlayData, CallbackInfo ci) {
        Screen screen = CompatibilityWarningScreen.tryOpen();
        if (screen != null) {
            this.setScreen(screen);
            ci.cancel();
        }
    }
}
