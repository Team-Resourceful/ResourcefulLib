package com.teamresourceful.resourcefullib.common.bytecodecs;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;

import java.io.IOException;
import java.util.Optional;

public final class CompoundTagByteCodec implements ByteCodec<Optional<CompoundTag>> {

    public static final CompoundTagByteCodec INSTANCE = new CompoundTagByteCodec();

    @Override
    public void encode(Optional<CompoundTag> value, ByteBuf buffer) {
        if (value.isEmpty()) {
            buffer.writeByte(0);
        } else {
            try {
                NbtIo.write(value.get(), new ByteBufOutputStream(buffer));
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }

    }

    @Override
    public Optional<CompoundTag> decode(ByteBuf buffer) {
        int i = buffer.readerIndex();
        byte b = buffer.readByte();
        if (b == 0) {
            return Optional.empty();
        } else {
            buffer.readerIndex(i);

            try {
                return Optional.of(NbtIo.read(new ByteBufInputStream(buffer), new NbtAccounter(2097152L)));
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}