package com.teamresourceful.resourcefullib.common.network.fabric;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.Networking;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record FabricClientNetworking(ResourceLocation channel) implements Networking {

    @Override
    public <T extends Packet<T>> void register(ClientboundPacketType<T> type) {
        ClientPlayNetworking.registerGlobalReceiver(createChannelLocation(channel, type.id()), (client, handler1, buf, responseSender) -> {
            T decode = type.decode(buf);
            client.execute(() -> type.handle(decode).run());
        });
    }

    @Override
    public <T extends Packet<T>> void register(ServerboundPacketType<T> type) {
        throw new IllegalStateException("Server packets should not be registered on the client!");
    }

    @Override
    public <T extends Packet<T>> void sendToServer(T message) {
        PacketType<T> type = message.type();
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        type.encode(message, buf);
        ClientPlayNetworking.send(createChannelLocation(channel, type.id()), buf);
    }

    @Override
    public <T extends Packet<T>> void sendToPlayer(T message, ServerPlayer player) {
        throw new IllegalStateException("Client packets should not be sent from the client!");
    }

    @Override
    public boolean canSendToPlayer(ServerPlayer player) {
        throw new IllegalStateException("Client packets should not be sent from the client!");
    }

    private static ResourceLocation createChannelLocation(ResourceLocation channel, ResourceLocation id) {
        return new ResourceLocation(channel.getNamespace(), channel.getPath() + "/" + id.getNamespace() + "/" + id.getPath());
    }
}
