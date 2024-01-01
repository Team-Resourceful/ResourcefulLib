package com.teamresourceful.resourcefullib.common.network.forge;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.Networking;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class ForgeNetworking implements Networking {

    private final SimpleChannel channel;

    public ForgeNetworking(ResourceLocation name, int protocolVersion, boolean optional) {
        Channel.VersionTest test = optional ?
                Channel.VersionTest.ACCEPT_MISSING.or(Channel.VersionTest.ACCEPT_VANILLA) :
                Channel.VersionTest.exact(protocolVersion);

        this.channel = ChannelBuilder.named(name)
                .networkProtocolVersion(protocolVersion)
                .acceptedVersions(test)
                .simpleChannel();
    }

    @Override
    public <T extends Packet<T>> void register(ClientboundPacketType<T> type) {
        this.channel.messageBuilder(type.type())
                .decoder(type::decode)
                .encoder(type::encode)
                .consumerNetworkThread((msg, ctx) -> {
                    ctx.enqueueWork(() -> type.handle(msg).run());
                    ctx.setPacketHandled(true);
                })
                .add();
    }

    @Override
    public <T extends Packet<T>> void register(ServerboundPacketType<T> type) {
        this.channel.messageBuilder(type.type())
                .decoder(type::decode)
                .encoder(type::encode)
                .consumerNetworkThread((msg, ctx) -> {
                    ctx.enqueueWork(() -> type.handle(msg).accept(ctx.getSender()));
                    ctx.setPacketHandled(true);
                })
                .add();
    }

    @Override
    public <T extends Packet<T>> void sendToServer(T message) {
        this.channel.send(message, PacketDistributor.SERVER.noArg());
    }

    @Override
    public <T extends Packet<T>> void sendToPlayer(T message, ServerPlayer player) {
        this.channel.send(message, PacketDistributor.PLAYER.with(player));
    }

    @Override
    public boolean canSendToPlayer(ServerPlayer player, PacketType<?> type) {
        return channel.isRemotePresent(player.connection.getConnection());
    }
}
