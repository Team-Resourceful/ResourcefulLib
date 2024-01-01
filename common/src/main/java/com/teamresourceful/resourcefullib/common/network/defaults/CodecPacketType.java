package com.teamresourceful.resourcefullib.common.network.defaults;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public abstract class CodecPacketType<T extends Packet<T>> extends AbstractPacketType<T> {

    protected ByteCodec<T> codec;

    public CodecPacketType(Class<T> clazz, ResourceLocation id, ByteCodec<T> codec) {
        super(clazz, id);
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
