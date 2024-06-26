package com.teamresourceful.resourcefullib.common.network.base;

import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import com.teamresourceful.resourcefullib.common.network.Packet;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * These are helper methods to allow for the creation of handlers with more defined processes.
 */
public final class NetworkHandle {

    private NetworkHandle() throws UtilityClassException {
        throw new UtilityClassException();
    }

    public static <T extends Packet<T>> Function<T, Runnable> handle(Consumer<T> handler) {
        return message -> () -> handler.accept(message);
    }

    public static <T extends Packet<T>, O> Function<T, Runnable> handle(Function<T, O> processor, Consumer<O> handler) {
        return message -> {
            var processed = processor.apply(message);
            return () -> handler.accept(processed);
        };
    }

    public static <T extends Packet<T>> Function<T, Consumer<Player>> handle(BiConsumer<T, Player> handler) {
        return message -> player -> handler.accept(message, player);
    }

    public static <T extends Packet<T>, O> Function<T, Consumer<Player>> handle(Function<T, O> processor, BiConsumer<O, Player> handler) {
        return message -> {
            var processed = processor.apply(message);
            return player -> handler.accept(processed, player);
        };
    }
}
