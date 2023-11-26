package com.teamresourceful.resourcefullib.common.network.base;

import com.teamresourceful.resourcefullib.common.network.Packet;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

@ApiStatus.NonExtendable
public interface ServerboundPacketType<T extends Packet<T>> extends PacketType<T> {
    Consumer<Player> handle(T message);
}
