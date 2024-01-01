package com.teamresourceful.resourcefullib.common.network.fabric;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.Networking;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record FabricServerNetworking(ResourceLocation channel) implements Networking {

    @Override
    public <T extends Packet<T>> void register(ClientboundPacketType<T> type) {
        throw new IllegalStateException("Client packets should not be registered on the server!");
    }

    @Override
    public <T extends Packet<T>> void register(ServerboundPacketType<T> type) {
        ResourceLocation location = createChannelLocation(channel, type.id());
        ServerPlayNetworking.registerGlobalReceiver(location, (server, player, handler1, buf, responseSender) -> {
            T decode = type.decode(buf);
            server.execute(() -> type.handle(decode).accept(player));
        });
    }

    @Override
    public <T extends Packet<T>> void sendToServer(T message) {
        throw new IllegalStateException("Server packets should not be sent from the server!");
    }

    @Override
    public <T extends Packet<T>> void sendToPlayer(T message, ServerPlayer player) {
        PacketType<T> type = message.type();
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        type.encode(message, buf);
        ServerPlayNetworking.send(player, createChannelLocation(channel, type.id()), buf);
    }

    @Override
    public boolean canSendToPlayer(ServerPlayer player, PacketType<?> type) {
        return ServerPlayNetworking.canSend(player, createChannelLocation(channel, type.id()));
    }

    private static ResourceLocation createChannelLocation(ResourceLocation channel, ResourceLocation id) {
        return channel.withSuffix("/" + id.getNamespace() + "/" + id.getPath());
    }
}
