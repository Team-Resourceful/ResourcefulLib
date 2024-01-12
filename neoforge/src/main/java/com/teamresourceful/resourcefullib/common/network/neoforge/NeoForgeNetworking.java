package com.teamresourceful.resourcefullib.common.network.neoforge;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.Networking;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class NeoForgeNetworking implements Networking {

    private static final List<Consumer<RegisterPayloadHandlerEvent>> LISTENERS = Collections.synchronizedList(new ArrayList<>());

    private final List<ClientboundPacketType<?>> clientPackets = new ArrayList<>();
    private final List<ServerboundPacketType<?>> serverPackets = new ArrayList<>();

    private final ResourceLocation channel;
    private final boolean optional;

    public NeoForgeNetworking(ResourceLocation channel, int protocolVersion, boolean optional) {
        this.channel = channel.withSuffix("/v" + protocolVersion);
        this.optional = optional;

        LISTENERS.add(this::onNetworkSetup);
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
        ResourceLocation id = createChannelLocation(this.channel, message.type().id());
        PacketDistributor.SERVER.noArg().send(new NeoForgeCustomPayload<>(message, id));
    }

    @Override
    public <T extends Packet<T>> void sendToPlayer(T message, ServerPlayer player) {
        ResourceLocation id = createChannelLocation(this.channel, message.type().id());
        PacketDistributor.PLAYER.with(player).send(new NeoForgeCustomPayload<>(message, id));
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
        ResourceLocation id = createChannelLocation(this.channel, type.id());
        registrar.common(
            id,
            NeoForgeCustomPayload.read(type, id),
            NeoForgeCustomPayload.handleClient(type)
        );
    }

    private <T extends Packet<T>> void registerServerbound(IPayloadRegistrar registrar, ServerboundPacketType<T> type) {
        ResourceLocation id = createChannelLocation(this.channel, type.id());
        registrar.common(
            id,
            NeoForgeCustomPayload.read(type, id),
            NeoForgeCustomPayload.handleServer(type)
        );
    }

    private static ResourceLocation createChannelLocation(ResourceLocation channel, ResourceLocation id) {
        return channel.withSuffix("/" + id.getNamespace() + "/" + id.getPath());
    }

    public static void setupNetwork(RegisterPayloadHandlerEvent event) {
        LISTENERS.forEach(listener -> listener.accept(event));
    }
}
