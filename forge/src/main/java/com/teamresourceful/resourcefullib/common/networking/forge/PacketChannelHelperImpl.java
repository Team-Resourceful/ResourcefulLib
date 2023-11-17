package com.teamresourceful.resourcefullib.common.networking.forge;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class PacketChannelHelperImpl {

    public static final Map<ResourceLocation, SimpleChannel> CHANNELS = new HashMap<>();

    public static void registerChannel(ResourceLocation name, int protocolVersion, BooleanSupplier optional) {
        Channel.VersionTest test = optional.getAsBoolean() ?
                Channel.VersionTest.ACCEPT_MISSING.or(Channel.VersionTest.ACCEPT_VANILLA) :
                Channel.VersionTest.exact(protocolVersion);

        CHANNELS.put(name, ChannelBuilder.named(name)
                .networkProtocolVersion(protocolVersion)
                .acceptedVersions(test)
                .simpleChannel());
    }

    public static <T extends Packet<T>> void registerS2CPacket(ResourceLocation name, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        SimpleChannel channel = CHANNELS.get(name);
        if (channel == null) {
            throw new IllegalStateException("Channel " + name + " not registered");
        }
        channel.messageBuilder(packetClass)
            .decoder(handler::decode)
            .encoder(handler::encode)
            .consumerNetworkThread((msg, context) -> {
                Player player = context.getSender() == null ? getPlayer() : null;
                if (player != null) {
                    context.enqueueWork(() -> handler.handle(msg).apply(player, player.level()));
                }
                context.setPacketHandled(true);
            })
            .add();
    }

    public static <T extends Packet<T>> void registerC2SPacket(ResourceLocation name, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        SimpleChannel channel = CHANNELS.get(name);
        if (channel == null) {
            throw new IllegalStateException("Channel " + name + " not registered");
        }
        channel.messageBuilder(packetClass)
                .decoder(handler::decode)
                .encoder(handler::encode)
                .consumerNetworkThread((msg, context) -> {
                    Player player = context.getSender();
                    if (player != null) {
                        context.enqueueWork(() -> handler.handle(msg).apply(player, player.level()));
                    }
                    context.setPacketHandled(true);
                })
                .add();
    }

    @OnlyIn(Dist.CLIENT)
    private static Player getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static <T extends Packet<T>> void sendToServer(ResourceLocation name, T packet) {
        SimpleChannel channel = CHANNELS.get(name);
        if (channel == null) {
            throw new IllegalStateException("Channel " + name + " not registered");
        }
        channel.send(packet, PacketDistributor.SERVER.noArg());
    }

    public static <T extends Packet<T>> void sendToPlayer(ResourceLocation name, T packet, Player player) {
        SimpleChannel channel = CHANNELS.get(name);
        if (channel == null) {
            throw new IllegalStateException("Channel " + name + " not registered");
        }
        if (player instanceof ServerPlayer serverPlayer) {
            channel.send(packet, PacketDistributor.PLAYER.with(serverPlayer));
        }
    }

    public static boolean canSendPlayerPackets(ResourceLocation name, Player player) {
        SimpleChannel channel = CHANNELS.get(name);
        if (channel != null && player instanceof ServerPlayer serverPlayer) {
            return channel.isRemotePresent(serverPlayer.connection.getConnection());
        }
        return false;
    }
}
