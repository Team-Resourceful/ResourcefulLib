package com.teamresourceful.resourcefullib.common.networking.fabric;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketChannelHelperImpl {

    public static void registerChannel(ResourceLocation channel, int protocolVersion) {
        //Do nothing as fabric uses the custom packet channel.
    }

    public static <T extends Packet<T>> void registerS2CPacket(ResourceLocation channel, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT))
            clientOnlyRegister(createChannelLocation(channel, id), handler);
    }

    @Environment(EnvType.CLIENT)
    private static <T extends Packet<T>> void clientOnlyRegister(ResourceLocation location, PacketHandler<T> handler) {
        ClientPlayNetworking.registerGlobalReceiver(location, (client, handler1, buf, responseSender) -> {
            T decode = handler.decode(buf);
            client.execute(() -> handler.handle(decode).apply(client.player, client.level));
        });
    }

    public static <T extends Packet<T>> void registerC2SPacket(ResourceLocation channel, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        ServerPlayNetworking.registerGlobalReceiver(createChannelLocation(channel, id), (server, player, handler1, buf, responseSender) -> {
            T decode = handler.decode(buf);
            server.execute(() -> handler.handle(decode).apply(player, player.level));
        });
    }

    public static <T extends Packet<T>> void sendToServer(ResourceLocation channel, T packet) {
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT))
            sendToServerClientOnly(channel, packet);
    }

    @Environment(EnvType.CLIENT)
    private static <T extends Packet<T>> void sendToServerClientOnly(ResourceLocation channel, T packet) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        packet.getHandler().encode(packet, buf);
        ClientPlayNetworking.send(createChannelLocation(channel, packet.getID()), buf);
    }

    public static <T extends Packet<T>> void sendToPlayer(ResourceLocation channel, T packet, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            packet.getHandler().encode(packet, buf);
            ServerPlayNetworking.send(serverPlayer, createChannelLocation(channel, packet.getID()), buf);
        }
    }

    private static ResourceLocation createChannelLocation(ResourceLocation channel, ResourceLocation id) {
        return new ResourceLocation(channel.getNamespace(), channel.getPath() + "/" + id.getNamespace() + "/" + id.getPath());
    }

}
