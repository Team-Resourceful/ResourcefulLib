package com.teamresourceful.resourcefullib.common.network;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.Networking;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.util.Collection;

public class Network implements Networking {

    private final Networking networking;
    private final boolean optional;

    public Network(ResourceLocation channel, int protocolVersion) {
        this(channel, protocolVersion, false);
    }

    public Network(ResourceLocation channel, int protocolVersion, boolean optional) {
        this.networking = getNetwork(channel, protocolVersion, optional);
        this.optional = optional;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.22")
    public Network(String modid, int protocolVersion, String channel) {
        this(modid, protocolVersion, channel, false);
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.22")
    public Network(String modid, int protocolVersion, String channel, boolean optional) {
        this.networking = getNetwork(ResourceLocation.fromNamespaceAndPath(modid, channel), protocolVersion, optional);
        this.optional = optional;
    }

    @Override
    public final <T extends Packet<T>> void register(ClientboundPacketType<T> type) {
        this.networking.register(type);
    }

    @Override
    public final <T extends Packet<T>> void register(ServerboundPacketType<T> type) {
        this.networking.register(type);
    }

    @Override
    public final <T extends Packet<T>> void sendToServer(T message) {
        this.networking.sendToServer(message);
    }

    @Override
    public final <T extends Packet<T>> void sendToPlayer(T message, ServerPlayer player) {
        if (optional && !canSendToPlayer(player, message.type())) return;
        this.networking.sendToPlayer(message, player);
    }

    public final <T extends Packet<T>> void sendToPlayer(T message, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            sendToPlayer(message, serverPlayer);
        }
    }

    public final <T extends Packet<T>> void sendToPlayers(T message, Collection<? extends Player> players) {
        players.forEach(player -> sendToPlayer(message, player));
    }

    public final <T extends Packet<T>> void sendToAllPlayers(T message, MinecraftServer server) {
        sendToPlayers(message, server.getPlayerList().getPlayers());
    }

    public final <T extends Packet<T>> void sendToPlayersInLevel(T message, Level level) {
        sendToPlayers(message, level.players());
    }

    public final <T extends Packet<T>> void sendToAllLoaded(T message, Level level, BlockPos pos) {
        LevelChunk chunk = level.getChunkAt(pos);
        if (chunk != null && level.getChunkSource() instanceof ServerChunkCache serverCache) {
            serverCache.chunkMap.getPlayers(chunk.getPos(), false).forEach(player -> sendToPlayer(message, player));
        }
    }

    public final <T extends Packet<T>> void sendToPlayersInRange(T message, Level level, BlockPos pos, double range) {
        for (Player player : level.players()) {
            if (player.blockPosition().distSqr(pos) <= range) {
                sendToPlayer(message, player);
            }
        }
    }

    /**
     * Checks if the player can receive packets from this channel.
     *
     * @param player The player to check.
     * @return True if the player can receive packets from this channel, false otherwise.
     *
     * @implNote On forge this will only check if it has the channel not if it can receive that specific packet.
     */
    @Override
    public final boolean canSendToPlayer(ServerPlayer player, PacketType<?> type) {
        return this.networking.canSendToPlayer(player, type);
    }

    public final boolean canSendToPlayer(Player player, PacketType<?> type) {
        return player instanceof ServerPlayer serverPlayer && canSendToPlayer(serverPlayer, type);
    }

    @ExpectPlatform
    @ApiStatus.Internal
    @Contract(pure = true)
    public static Networking getNetwork(ResourceLocation channel, int protocolVersion, boolean optional) {
        throw new NotImplementedException();
    }
}
