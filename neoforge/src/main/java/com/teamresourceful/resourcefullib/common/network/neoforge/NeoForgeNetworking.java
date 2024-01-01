package com.teamresourceful.resourcefullib.common.network.neoforge;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.Networking;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import com.teamresourceful.resourcefullib.neoforge.ResourcefulLibNeoForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeNetworking implements Networking {

    private final List<ClientboundPacketType<?>> clientPackets = new ArrayList<>();
    private final List<ServerboundPacketType<?>> serverPackets = new ArrayList<>();

    private final ResourceLocation channel;
    private final boolean optional;

    public NeoForgeNetworking(ResourceLocation channel, int protocolVersion, boolean optional) {
        this.channel = new ResourceLocation(channel.getNamespace(), channel.getPath() + "/v" + protocolVersion);
        this.optional = optional;

        ResourcefulLibNeoForge.listen(bus -> bus.addListener(RegisterPayloadHandlerEvent.class, this::onNetworkSetup));
    }

    @Override
    public <T extends Packet<T>> void register(ClientboundPacketType<T> type) {
        this.clientPackets.add(type);
    }

    @Override
    public <T extends Packet<T>> void register(ServerboundPacketType<T> type) {
        this.serverPackets.add(type);
    }

    @Override
    public <T extends Packet<T>> void sendToServer(T message) {
        PacketDistributor.SERVER.noArg().send(new NeoForgeCustomPayload<>(message));
    }

    @Override
    public <T extends Packet<T>> void sendToPlayer(T message, ServerPlayer player) {
        PacketDistributor.PLAYER.with(player).send(new NeoForgeCustomPayload<>(message));
    }

    @Override
    public boolean canSendToPlayer(ServerPlayer player, PacketType<?> type) {
        return player.connection.isConnected(createChannelLocation(this.channel, type.id()));
    }

    public void onNetworkSetup(RegisterPayloadHandlerEvent event) {
        IPayloadRegistrar registrar = event.registrar(this.channel.getNamespace());
        if (this.optional) {
            registrar = registrar.optional();
        }

        for (ClientboundPacketType<?> type : this.clientPackets) {
            registerClientbound(registrar, type);
        }

        for (ServerboundPacketType<?> type : this.serverPackets) {
            registerServerbound(registrar, type);
        }
    }

    private <T extends Packet<T>> void registerClientbound(IPayloadRegistrar registrar, ClientboundPacketType<T> type) {
        registrar.common(
            createChannelLocation(this.channel, type.id()),
            NeoForgeCustomPayload.read(type),
            NeoForgeCustomPayload.handleClient(type)
        );
    }

    private <T extends Packet<T>> void registerServerbound(IPayloadRegistrar registrar, ServerboundPacketType<T> type) {
        registrar.common(
            createChannelLocation(this.channel, type.id()),
            NeoForgeCustomPayload.read(type),
            NeoForgeCustomPayload.handleServer(type)
        );
    }

    private static ResourceLocation createChannelLocation(ResourceLocation channel, ResourceLocation id) {
        return new ResourceLocation(channel.getNamespace(), channel.getPath() + "/" + id.getNamespace() + "/" + id.getPath());
    }
}
