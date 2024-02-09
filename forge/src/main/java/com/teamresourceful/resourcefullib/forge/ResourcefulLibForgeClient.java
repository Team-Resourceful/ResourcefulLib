package com.teamresourceful.resourcefullib.forge;

import com.teamresourceful.resourcefullib.client.compatibility.CompatibilityWarningScreen;
import com.teamresourceful.resourcefullib.client.highlights.HighlightHandler;
import net.minecraft.client.gui.screens.AccessibilityOnboardingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ResourcefulLibForgeClient {

    public static void init() {
        ForgeResourcePackHandler.load();
        MinecraftForge.EVENT_BUS.addListener(ResourcefulLibForgeClient::onHighlight);
        MinecraftForge.EVENT_BUS.addListener(ResourcefulLibForgeClient::onScreenChange);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ResourcefulLibForgeClient::onClientReloadListeners);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ForgeResourcePackHandler::onRegisterPackFinders);
    }

    public static void onClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new HighlightHandler());
    }

    public static void onHighlight(RenderHighlightEvent.Block event) {
        BlockState state = event.getCamera().getEntity().level().getBlockState(event.getTarget().getBlockPos());
        event.setCanceled(HighlightHandler.onBlockHighlight(event.getCamera().getPosition(), event.getCamera().getEntity(), event.getPoseStack(), event.getTarget().getBlockPos(), state, event.getMultiBufferSource().getBuffer(RenderType.lines())));
    }

    public static void onScreenChange(ScreenEvent.Opening event) {
        if (event.getScreen() instanceof TitleScreen || event.getScreen() instanceof AccessibilityOnboardingScreen) {
            Screen screen = CompatibilityWarningScreen.tryOpen();
            if (screen == null) return;
            event.setNewScreen(screen);
        }
    }
}
