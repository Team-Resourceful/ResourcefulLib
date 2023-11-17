package com.teamresourceful.resourcefullib.forge;

import com.teamresourceful.resourcefullib.client.highlights.HighlightHandler;
import com.teamresourceful.resourcefullib.client.sysinfo.SystemInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ResourcefulLibForgeClient {

    public static void init() {
        ForgeResourcePackHandler.load();
        MinecraftForge.EVENT_BUS.addListener(ResourcefulLibForgeClient::onHighlight);
        MinecraftForge.EVENT_BUS.addListener(ResourcefulLibForgeClient::onClientCommandRegister);
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
