package com.teamresourceful.resourcefullib.forge;

import com.teamresourceful.resourcefullib.client.highlights.HighlightHandler;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ResourcefulLibForgeClient {

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(ResourcefulLibForgeClient::onHighlight);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ResourcefulLibForgeClient::onClientReloadListeners);
    }

    public static void onClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new HighlightHandler());
    }

    public static void onHighlight(RenderHighlightEvent.Block event) {
        BlockState state = event.getCamera().getEntity().level().getBlockState(event.getTarget().getBlockPos());
        event.setCanceled(HighlightHandler.onBlockHighlight(event.getCamera().getPosition(), event.getCamera().getEntity(), event.getPoseStack(), event.getTarget().getBlockPos(), state, event.getMultiBufferSource().getBuffer(RenderType.lines())));
    }
}
