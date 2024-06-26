package com.teamresourceful.resourcefullib.common.bytecodecs;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class StreamCodecByteCodec {

    private static <B extends ByteBuf, T> ByteCodec<T> of(StreamCodec<B, T> codec, Function<ByteBuf, B> mapper) {
        return ByteCodec.passthrough(
                (buf, value) -> codec.encode(mapper.apply(buf), value),
                buffer -> codec.decode(mapper.apply(buffer))
        );
    }

    private static <B extends ByteBuf, T> StreamCodec<B, T> to(ByteCodec<T> codec, Function<B, ByteBuf> mapper) {
        return StreamCodec.of(
                (buf, value) -> codec.encode(value, mapper.apply(buf)),
                (buf) -> codec.decode(mapper.apply(buf))
        );
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
