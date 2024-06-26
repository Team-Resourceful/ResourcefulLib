package com.teamresourceful.resourcefullib.common.network.defaults;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.resourcefullib.common.bytecodecs.StreamCodecByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class CodecPacketType<T extends Packet<T>> extends AbstractPacketType<T> {

    protected StreamCodec<RegistryFriendlyByteBuf, T> codec;

    public CodecPacketType(ResourceLocation id, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        super(id);
        this.codec = codec;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "22.0")
    public CodecPacketType(Class<T> clazz, ResourceLocation id, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        super(clazz, id);
        this.codec = codec;
    }

    public CodecPacketType(ResourceLocation id, ByteCodec<T> codec) {
        this(id, StreamCodecByteCodec.toRegistry(codec));
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "22.0")
    public CodecPacketType(Class<T> clazz, ResourceLocation id, ByteCodec<T> codec) {
        this(clazz, id, StreamCodecByteCodec.toRegistry(codec));
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

        public Client(ResourceLocation id, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
            super(id, codec);
        }

        public Client(ResourceLocation id, ByteCodec<T> codec) {
            super(id, codec);
        }

        public static <T extends Packet<T>> Client<T> create(
                ResourceLocation id,
                StreamCodec<RegistryFriendlyByteBuf, T> codec,
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
                ResourceLocation id,
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

        public Server(ResourceLocation id, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
            super(id, codec);
        }

        public Server(ResourceLocation id, ByteCodec<T> codec) {
            super(id, codec);
        }

        public static <T extends Packet<T>> Server<T> create(
                ResourceLocation id,
                StreamCodec<RegistryFriendlyByteBuf, T> codec,
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
                ResourceLocation id,
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
