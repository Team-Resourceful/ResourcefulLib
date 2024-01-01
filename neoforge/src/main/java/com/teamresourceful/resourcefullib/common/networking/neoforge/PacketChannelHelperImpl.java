package com.teamresourceful.resourcefullib.common.networking.neoforge;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacketChannelHelperImpl {

    private static final Map<ResourceLocation, Channel> CHANNELS = new HashMap<>();

    public static void registerChannel(ResourceLocation channel, int protocolVersion, boolean optional) {
        CHANNELS.put(channel, new Channel(
                new ResourceLocation(channel.getNamespace(), channel.getPath() + "/v" + protocolVersion),
                optional
        ));
    }

    public static <T extends Packet<T>> void registerS2CPacket(ResourceLocation name, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        Channel channel = CHANNELS.get(name);
        if (channel == null) {
            throw new IllegalStateException("Channel " + name + " not registered");
        }
        channel.clientPackets.add(new Type<>(id, handler, packetClass, PacketFlow.CLIENTBOUND));
    }

    public static <T extends Packet<T>> void registerC2SPacket(ResourceLocation name, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        Channel channel = CHANNELS.get(name);
        if (channel == null) {
            throw new IllegalStateException("Channel " + name + " not registered");
        }
        channel.serverPackets.add(new Type<>(id, handler, packetClass, PacketFlow.SERVERBOUND));
    }

    public static <T extends Packet<T>> void sendToServer(ResourceLocation name, T packet) {
        Channel channel = CHANNELS.get(name);
        if (channel == null) {
            throw new IllegalStateException("Channel " + name + " not registered");
        }
        PacketDistributor.SERVER.noArg().send(new NeoForgeCustomPayload<>(packet));
    }

    public static <T extends Packet<T>> void sendToPlayer(ResourceLocation name, T packet, Player player) {
        Channel channel = CHANNELS.get(name);
        if (channel == null) {
            throw new IllegalStateException("Channel " + name + " not registered");
        }
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.PLAYER.with(serverPlayer).send(new NeoForgeCustomPayload<>(packet));
        }
    }

    public static boolean canSendPlayerPackets(ResourceLocation name, Player player) {
        return false;
    }

    public void onNetworkSetup(RegisterPayloadHandlerEvent event) {
        for (Channel value : CHANNELS.values()) {
            ResourceLocation channel = value.channel;
            IPayloadRegistrar registrar = event.registrar(channel.getNamespace());
            if (value.optional) {
                registrar = registrar.optional();
            }

            for (Type<?> type : value.clientPackets) {
                type.register(registrar, channel);
            }

            for (Type<?> type : value.serverPackets) {
                type.register(registrar, channel);
            }
        }

    }

    private static final class Channel {
        private final List<Type<?>> clientPackets = new ArrayList<>();
        private final List<Type<?>> serverPackets = new ArrayList<>();

        private final ResourceLocation channel;
        private final boolean optional;

        private Channel(ResourceLocation channel, boolean optional) {
            this.channel = channel;
            this.optional = optional;
        }
    }

    private record Type<T extends Packet<T>>(ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass, PacketFlow flow) {
        private void register(IPayloadRegistrar registrar, ResourceLocation channel) {
            registrar.common(
                    createChannelLocation(channel, id),
                    NeoForgeCustomPayload.read(handler),
                    NeoForgeCustomPayload.handle(handler, flow)
            );
        }
    }

    private static ResourceLocation createChannelLocation(ResourceLocation channel, ResourceLocation id) {
        return new ResourceLocation(channel.getNamespace(), channel.getPath() + "/" + id.getNamespace() + "/" + id.getPath());
    }
}
