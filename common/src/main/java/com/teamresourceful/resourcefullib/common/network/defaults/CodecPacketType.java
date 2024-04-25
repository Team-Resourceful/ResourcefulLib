package com.teamresourceful.resourcefullib.common.network.defaults;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.resourcefullib.common.bytecodecs.StreamCodecByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public abstract class CodecPacketType<T extends Packet<T>> extends AbstractPacketType<T> {

    protected StreamCodec<RegistryFriendlyByteBuf, T> codec;

    public CodecPacketType(Class<T> clazz, ResourceLocation id, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        super(clazz, id);
        this.codec = codec;
    }

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
}
