package com.teamresourceful.resourcefullib.neoforge;

import com.teamresourceful.resourcefullib.ResourcefulLib;
import com.teamresourceful.resourcefullib.client.fluid.neoforge.ResourcefulClientFluidType;
import com.teamresourceful.resourcefullib.client.fluid.registry.ResourcefulClientFluidRegistry;
import com.teamresourceful.resourcefullib.client.highlights.HighlightHandler;
import com.teamresourceful.resourcefullib.client.sysinfo.SystemInfo;
import com.teamresourceful.resourcefullib.common.fluid.neoforge.ResourcefulFluidType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.CommonColors;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.ExtractBlockOutlineRenderStateEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
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

    public static void onClientReloadListeners(AddClientReloadListenersEvent event) {
        event.addListener(Identifier.fromNamespaceAndPath(ResourcefulLib.MOD_ID, "highlights"), new HighlightHandler());
    }

    public static void onHighlight(ExtractBlockOutlineRenderStateEvent event) {
        final var pos = event.getBlockPos();
        final var state = HighlightHandler.extractState(event.getLevel(), pos, event.getBlockState());

        if (state == null) return;

        event.addCustomRenderer((outlineState, buffer, stack, pass, levelState) -> {
            if (outlineState.highContrast()) {
                HighlightHandler.onBlockHighlight(
                        event.getCamera().position(),
                        stack,
                        pos,
                        state,
                        buffer.getBuffer(RenderTypes.secondaryBlockOutline()),
                        CommonColors.BLACK,
                        7f
                );
            }

            return HighlightHandler.onBlockHighlight(
                    event.getCamera().position(),
                    stack,
                    pos,
                    state,
                    buffer.getBuffer(RenderTypes.lines()),
                    outlineState.highContrast() ? CommonColors.HIGH_CONTRAST_DIAMOND : ARGB.color(102, CommonColors.BLACK),
                    Minecraft.getInstance().getWindow().getAppropriateLineWidth()
            );
        });
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
            var id = entry.getKey().identifier();
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
