package com.teamresourceful.resourcefullib.common.network.neoforge;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import org.jetbrains.annotations.NotNull;

public record NeoForgeCustomPayload<T extends Packet<T>>(T packet, ResourceLocation id) implements CustomPacketPayload {

    public static <T extends Packet<T>> FriendlyByteBuf.Reader<NeoForgeCustomPayload<T>> read(PacketType<T> type, ResourceLocation id) {
        return buf -> new NeoForgeCustomPayload<>(type.decode(buf), id);
    }

    public static <T extends Packet<T>> IPayloadHandler<NeoForgeCustomPayload<T>> handleClient(ClientboundPacketType<T> type) {
        return (packet, context) -> context.workHandler().submitAsync(() -> type.handle(packet.packet()).run());
    }

    public static <T extends Packet<T>> IPayloadHandler<NeoForgeCustomPayload<T>> handleServer(ServerboundPacketType<T> type) {
        return (packet, context) -> context.player()
                .ifPresentOrElse(
                    player -> context.workHandler().submitAsync(() -> type.handle(packet.packet()).accept(player)),
                    () -> {
                        throw new IllegalStateException("Received a serverbound packet without a player context.");
                    }
                );
    }

    @Override
    public void write(@NotNull FriendlyByteBuf arg) {
        packet().type().encode(packet(), arg);
    }
}
