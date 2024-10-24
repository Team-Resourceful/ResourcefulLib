package com.teamresourceful.resourcefullib.neoforge;

import com.teamresourceful.resourcefullib.client.fluid.neoforge.ResourcefulClientFluidType;
import com.teamresourceful.resourcefullib.client.fluid.registry.ResourcefulClientFluidRegistry;
import com.teamresourceful.resourcefullib.client.highlights.HighlightHandler;
import com.teamresourceful.resourcefullib.client.sysinfo.SystemInfo;
import com.teamresourceful.resourcefullib.common.fluid.neoforge.ResourcefulFluidType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ResourcefulLibNeoForgeClient {

    public static void init(IEventBus modEventBus) {
        NeoForgeResourcePackHandler.load();
        NeoForge.EVENT_BUS.addListener(ResourcefulLibNeoForgeClient::onHighlight);
        NeoForge.EVENT_BUS.addListener(ResourcefulLibNeoForgeClient::onClientCommandRegister);
        modEventBus.addListener(ResourcefulLibNeoForgeClient::onRegisterFluidClient);
        modEventBus.addListener(ResourcefulLibNeoForgeClient::onClientReloadListeners);
        modEventBus.addListener(NeoForgeResourcePackHandler::onRegisterPackFinders);
    }

    public static void onClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new HighlightHandler());
    }

    public static void onHighlight(RenderHighlightEvent.Block event) {
        BlockState state = event.getCamera().getEntity().level().getBlockState(event.getTarget().getBlockPos());
        int color = Minecraft.getInstance().options.highContrastBlockOutline().get() ? 0xff57ffe1 : ARGB.color(102, 0xff000000);
        event.setCanceled(HighlightHandler.onBlockHighlight(
                event.getCamera().getPosition(),
                event.getCamera().getEntity(),
                event.getPoseStack(),
                event.getTarget().getBlockPos(),
                state,
                event.getMultiBufferSource().getBuffer(RenderType.lines()),
                color
        ));
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

    public static void onRegisterFluidClient(RegisterClientExtensionsEvent event) {
        for (var entry : NeoForgeRegistries.FLUID_TYPES.entrySet()) {
            var id = entry.getKey().location();
            var type = entry.getValue();
            if (type instanceof ResourcefulFluidType) {
                var properties = ResourcefulClientFluidRegistry.get(id);
                if (properties != null) {
                    event.registerFluidType(
                            new ResourcefulClientFluidType(properties),
                            type
                    );
                }
            }
        }
    }
}
