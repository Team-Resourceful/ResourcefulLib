package com.teamresourceful.resourcefullib.common.network.defaults;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.resourcefullib.common.bytecodecs.StreamCodecByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class CodecPacketType<T extends Packet<T>> extends AbstractPacketType<T> {

    protected StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull T> codec;

    public CodecPacketType(Identifier id, StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull T> codec) {
        super(id);
        this.codec = codec;
    }

    public CodecPacketType(Identifier id, ByteCodec<T> codec) {
        this(id, StreamCodecByteCodec.toRegistry(codec));
    }

    @Override
    public void encode(T message, RegistryFriendlyByteBuf buffer) {
        codec.encode(buffer, message);
    }

    @Override
    public T decode(RegistryFriendlyByteBuf buffer) {
        return codec.decode(buffer);
    }

    public static abstract class Client<T extends Packet<T>> extends CodecPacketType<T> implements ClientboundPacketType<T> {

        public Client(Identifier id, StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull T> codec) {
            super(id, codec);
        }

        public Client(Identifier id, ByteCodec<T> codec) {
            super(id, codec);
        }

        public static <T extends Packet<T>> Client<T> create(
                Identifier id,
                StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull T> codec,
                Function<T, Runnable> handler
        ) {
            return new Client<>(id, codec) {
                @Override
                public Runnable handle(T message) {
                    return handler.apply(message);
                }
            };
        }

        public static <T extends Packet<T>> Client<T> create(
                Identifier id,
                ByteCodec<T> codec,
                Function<T, Runnable> handler
        ) {
            return new Client<>(id, codec) {
                @Override
                public Runnable handle(T message) {
                    return handler.apply(message);
                }
            };
        }
    }

    public static abstract class Server<T extends Packet<T>> extends CodecPacketType<T> implements ServerboundPacketType<T> {

        public Server(Identifier id, StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull T> codec) {
            super(id, codec);
        }

        public Server(Identifier id, ByteCodec<T> codec) {
            super(id, codec);
        }

        public static <T extends Packet<T>> Server<T> create(
                Identifier id,
                StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull T> codec,
                Function<T, Consumer<Player>> handler
        ) {
            return new Server<>(id, codec) {
                @Override
                public Consumer<Player> handle(T message) {
                    return handler.apply(message);
                }
            };
        }

        public static <T extends Packet<T>> Server<T> create(
                Identifier id,
                ByteCodec<T> codec,
                Function<T, Consumer<Player>> handler
        ) {
            return new Server<>(id, codec) {
                @Override
                public Consumer<Player> handle(T message) {
                    return handler.apply(message);
                }
            };
        }
    }
}
