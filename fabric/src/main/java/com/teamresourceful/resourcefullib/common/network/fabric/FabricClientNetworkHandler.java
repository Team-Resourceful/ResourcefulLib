package com.teamresourceful.resourcefullib.common.network.fabric;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.internal.NetworkPacketPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class FabricClientNetworkHandler {

    public static <T extends Packet<T>> void register(CustomPacketPayload.Type<NetworkPacketPayload<T>> payloadType, ClientboundPacketType<T> type) {
        ClientPlayNetworking.registerGlobalReceiver(
            payloadType,
            (payload, context) -> type.handle(payload.packet()).run()
        );
    }

    public static <T extends Packet<T>> void send(ResourceLocation channel, T message) {
        ClientPlayNetworking.send(new NetworkPacketPayload<>(message, channel));
    }
}
