package com.teamresourceful.resourcefullib.common.networking;

import com.teamresourceful.resourcefullib.common.networking.base.NetworkDirection;
import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.Collection;
import java.util.function.BooleanSupplier;

public class NetworkChannel {

    private final ResourceLocation channel;

    /**
     * Creates a new network channel.
     * @param modid The modid of the mod.
     * @param protocolVersion The protocol version of the mod.
     * @param channel The channel name.
     */
    public NetworkChannel(String modid, int protocolVersion, String channel) {
        this.channel = new ResourceLocation(modid, channel);

        PacketChannelHelper.registerChannel(this.channel, protocolVersion, () -> false);
    }

    /**
     * Creates a new network channel.
     * @param modid The modid of the mod.
     * @param protocolVersion The protocol version of the mod.
     * @param channel The channel name.
     * @param optional If the channel is optional. Note: This is only for forge as fabric all channels are optional.
     */
    public NetworkChannel(String modid, int protocolVersion, String channel, boolean optional) {
        this.channel = new ResourceLocation(modid, channel);

        PacketChannelHelper.registerChannel(this.channel, protocolVersion, () -> optional);
    }

    /**
     * Creates a new network channel.
     * @param modid The modid of the mod.
     * @param protocolVersion The protocol version of the mod.
     * @param channel The channel name.
     * @param optional If the channel is optional. Note: This is only for forge as fabric all channels are optional.
     */
    public NetworkChannel(String modid, int protocolVersion, String channel, BooleanSupplier optional) {
        this.channel = new ResourceLocation(modid, channel);

        PacketChannelHelper.registerChannel(this.channel, protocolVersion, optional);
    }

    public final <T extends Packet<T>> void registerPacket(NetworkDirection direction, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        if (direction == NetworkDirection.CLIENT_TO_SERVER) {
            PacketChannelHelper.registerC2SPacket(this.channel, id, handler, packetClass);
        } else {
            PacketChannelHelper.registerS2CPacket(this.channel, id, handler, packetClass);
        }
    }

    public final <T extends Packet<T>> void sendToServer(T packet) {
        PacketChannelHelper.sendToServer(this.channel, packet);
    }

    public final <T extends Packet<T>> void sendToPlayer(T packet, Player player) {
        PacketChannelHelper.sendToPlayer(this.channel, packet, player);
    }

    public final <T extends Packet<T>> void sendToPlayers(T packet, Collection<? extends Player> players) {
        players.forEach(player -> sendToPlayer(packet, player));
    }

    public final <T extends Packet<T>> void sendToAllPlayers(T packet, MinecraftServer server) {
        sendToPlayers(packet, server.getPlayerList().getPlayers());
    }

    public final <T extends Packet<T>> void sendToPlayersInLevel(T packet, Level level) {
        sendToPlayers(packet, level.players());
    }

    public final <T extends Packet<T>> void sendToAllLoaded(T packet, Level level, BlockPos pos) {
        LevelChunk chunk = level.getChunkAt(pos);
        if (chunk != null && level.getChunkSource() instanceof ServerChunkCache serverCache) {
            serverCache.chunkMap.getPlayers(chunk.getPos(), false).forEach(player -> sendToPlayer(packet, player));
        }
    }

    public final <T extends Packet<T>> void sendToPlayersInRange(T packet, Level level, BlockPos pos, double range) {
        for (Player player : level.players()) {
            if (player.blockPosition().distSqr(pos) <= range) {
                sendToPlayer(packet, player);
            }
        }
    }

    public final boolean canSendPlayerPackets(Player player) {
        return PacketChannelHelper.canSendPlayerPackets(this.channel, player);
    }


}
