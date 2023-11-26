package com.teamresourceful.resourcefullib.common.network.forge;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.Networking;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BooleanSupplier;

public class ForgeNetworking implements Networking {

    private final SimpleChannel channel;
    private int packets = 0;

    public ForgeNetworking(ResourceLocation name, int protocolVersion, BooleanSupplier optional) {
        String version = Integer.toString(protocolVersion);
        this.channel = NetworkRegistry.newSimpleChannel(
                name,
                () -> Integer.toString(protocolVersion),
                v -> v.equals(version) || optional.getAsBoolean(),
                v -> v.equals(version) || optional.getAsBoolean()
        );
    }

    @Override
    public <T extends Packet<T>> void register(ClientboundPacketType<T> type) {
        this.channel.registerMessage(
                ++this.packets,
                type.type(),
                type::encode,
                type::decode,
                (msg, ctx) -> {
                    NetworkEvent.Context context = ctx.get();
                    context.enqueueWork(() -> type.handle(msg).run());
                    ctx.get().setPacketHandled(true);
                }
        );
    }

    @Override
    public <T extends Packet<T>> void register(ServerboundPacketType<T> type) {
        this.channel.registerMessage(
                ++this.packets,
                type.type(),
                type::encode,
                type::decode,
                (msg, ctx) -> {
                    NetworkEvent.Context context = ctx.get();
                    context.enqueueWork(() -> type.handle(msg).accept(context.getSender()));
                    context.setPacketHandled(true);
                }
        );
    }

    @Override
    public <T extends Packet<T>> void sendToServer(T message) {
        this.channel.sendToServer(message);
    }

    @Override
    public <T extends Packet<T>> void sendToPlayer(T message, ServerPlayer player) {
        this.channel.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    @Override
    public boolean canSendToPlayer(ServerPlayer player) {
        return channel.isRemotePresent(player.connection.connection);
    }
}
