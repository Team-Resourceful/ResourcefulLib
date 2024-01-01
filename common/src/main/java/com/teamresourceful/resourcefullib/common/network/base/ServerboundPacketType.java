package com.teamresourceful.resourcefullib.common.network.base;

import com.teamresourceful.resourcefullib.common.network.Packet;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public interface ServerboundPacketType<T extends Packet<T>> extends PacketType<T> {
    Consumer<Player> handle(T message);
}
