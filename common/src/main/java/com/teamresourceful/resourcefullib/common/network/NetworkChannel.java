package com.teamresourceful.resourcefullib.common.network;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.Networking;
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

import java.util.Collection;
import java.util.function.BooleanSupplier;

public class NetworkChannel implements Networking {

    private final Networking networking;

    public NetworkChannel(String modid, int protocolVersion, String channel) {
        this(modid, protocolVersion, channel, false);
    }

    public NetworkChannel(String modid, int protocolVersion, String channel, boolean optional) {
        this(modid, protocolVersion, channel, () -> optional);
    }

    public NetworkChannel(String modid, int protocolVersion, String channel, BooleanSupplier optional) {
        this.networking = getNetwork(new ResourceLocation(modid, channel), protocolVersion, optional);
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

    @Override
    public final boolean canSendToPlayer(ServerPlayer player) {
        return this.networking.canSendToPlayer(player);
    }

    public final boolean canSendToPlayer(Player player) {
        return player instanceof ServerPlayer serverPlayer && canSendToPlayer(serverPlayer);
    }

    @ExpectPlatform
    @ApiStatus.Internal
    public static Networking getNetwork(ResourceLocation channel, int protocolVersion, BooleanSupplier optional) {
        throw new NotImplementedException();
    }
}
