package com.teamresourceful.resourcefullib.common.networking.neoforge;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import org.jetbrains.annotations.NotNull;

public record NeoForgeCustomPayload<T extends Packet<T>>(T packet) implements CustomPacketPayload {

    public static <T extends Packet<T>> FriendlyByteBuf.Reader<NeoForgeCustomPayload<T>> read(PacketHandler<T> handler) {
        return buf -> new NeoForgeCustomPayload<>(handler.decode(buf));
    }

    @OnlyIn(Dist.CLIENT)
    public static Player getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static <T extends Packet<T>> IPayloadHandler<NeoForgeCustomPayload<T>> handle(PacketHandler<T> type, PacketFlow flow) {
        return (packet, context) -> {
            Player player = context.player().orElse(null);
            if (flow.isClientbound()) {
                player = getPlayer();
            }
            if (player == null) return;
            type.handle(packet.packet()).apply(player, player.level());
        };
    }

    @Override
    public void write(@NotNull FriendlyByteBuf arg) {
        packet().getHandler().encode(packet(), arg);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return packet().getID();
    }
}
