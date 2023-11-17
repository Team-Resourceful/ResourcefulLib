package com.teamresourceful.resourcefullib.neoforge;

import com.teamresourceful.resourcefullib.client.highlights.HighlightHandler;
import com.teamresourceful.resourcefullib.client.sysinfo.SystemInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ResourcefulLibNeoForgeClient {

    public static void init() {
        NeoForgeResourcePackHandler.load();
        NeoForge.EVENT_BUS.addListener(ResourcefulLibNeoForgeClient::onHighlight);
        NeoForge.EVENT_BUS.addListener(ResourcefulLibNeoForgeClient::onClientCommandRegister);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ResourcefulLibNeoForgeClient::onClientReloadListeners);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(NeoForgeResourcePackHandler::onRegisterPackFinders);
    }

    public static void onClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new HighlightHandler());
    }

    public static void onHighlight(RenderHighlightEvent.Block event) {
        BlockState state = event.getCamera().getEntity().level().getBlockState(event.getTarget().getBlockPos());
        event.setCanceled(HighlightHandler.onBlockHighlight(event.getCamera().getPosition(), event.getCamera().getEntity(), event.getPoseStack(), event.getTarget().getBlockPos(), state, event.getMultiBufferSource().getBuffer(RenderType.lines())));
    }

    public static void onClientCommandRegister(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("rlib")
                .then(Commands.literal("info")
                        .executes(context -> {
                            String info = SystemInfo.buildForDiscord();
                            Minecraft.getInstance().keyboardHandler.setClipboard(info);
                            context.getSource().sendSystemMessage(Component.literal("[Resourceful Lib]: Info copied to clipboard!"));
                            return 1;
                        })
                ));
    }
}
