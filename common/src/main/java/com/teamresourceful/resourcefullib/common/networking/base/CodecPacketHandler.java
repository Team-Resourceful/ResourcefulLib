package com.teamresourceful.resourcefullib.common.networking.base;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import net.minecraft.network.FriendlyByteBuf;

/**
 * This class is a {@link PacketHandler} that uses a {@link ByteCodec} to encode and decode packets.
 * <br>
 * @apiNote This class is experimental as the stability of the {@link ByteCodec} API is not guaranteed.
 *
 * @param <T> The type of packet this handler handles.
 */
public abstract class CodecPacketHandler<T extends Packet<T>> implements PacketHandler<T> {

    protected final ByteCodec<T> codec;

    public CodecPacketHandler(ByteCodec<T> codec) {
        this.codec = codec;
    }

    @Override
    public void encode(T message, FriendlyByteBuf buffer) {
        codec.encode(message, buffer);
    }

    @Override
    public T decode(FriendlyByteBuf buffer) {
        return codec.decode(buffer);
    }
}
