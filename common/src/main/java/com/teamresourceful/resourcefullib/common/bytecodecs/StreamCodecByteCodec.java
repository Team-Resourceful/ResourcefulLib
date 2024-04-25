package com.teamresourceful.resourcefullib.common.bytecodecs;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class StreamCodecByteCodec {

    private static <B extends ByteBuf, T> ByteCodec<T> of(StreamCodec<B, T> codec, Function<ByteBuf, B> mapper) {
        return new ByteCodec<>() {
            @Override
            public void encode(T value, ByteBuf buffer) {
                codec.encode(mapper.apply(buffer), value);
            }

            @Override
            public T decode(ByteBuf buffer) {
                return codec.decode(mapper.apply(buffer));
            }
        };
    }

    private static <B extends ByteBuf, T> StreamCodec<B, T> to(ByteCodec<T> codec, Function<B, ByteBuf> mapper) {
        return new StreamCodec<>() {
            @Override
            public @NotNull T decode(B byteBuf) {
                return codec.decode(mapper.apply(byteBuf));
            }

            @Override
            public void encode(B byteBuf, T t) {
                codec.encode(t, mapper.apply(byteBuf));
            }
        };
    }

    public static <T> ByteCodec<T> of(StreamCodec<ByteBuf, T> codec) {
        return of(codec, Function.identity());
    }

    public static <T> StreamCodec<ByteBuf, T> to(ByteCodec<T> codec) {
        return to(codec, Function.identity());
    }

    public static <T> ByteCodec<T> ofFriendly(StreamCodec<FriendlyByteBuf, T> codec) {
        return of(codec, ExtraByteCodecs::toFriendly);
    }

    public static <T> StreamCodec<FriendlyByteBuf, T> toFriendly(ByteCodec<T> codec) {
        return to(codec, ExtraByteCodecs::toFriendly);
    }

    public static <T> ByteCodec<T> ofRegistry(StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        return of(codec, ExtraByteCodecs::toRegistry);
    }

    public static <T> StreamCodec<RegistryFriendlyByteBuf, T> toRegistry(ByteCodec<T> codec) {
        return to(codec, ExtraByteCodecs::toRegistry);
    }

}
