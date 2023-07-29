package com.teamresourceful.resourcefullib.common.bytecodecs;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.IdMap;

public record IdMapByteCodec<T>(IdMap<T> map) implements ByteCodec<T> {

    @Override
    public void encode(T value, ByteBuf buffer) {
        int id = map.getId(value);
        if (id == -1) {
            throw new IllegalArgumentException("Can't find id for '" + value + "' in map " + map);
        }
        ByteBufUtils.writeVarInt(buffer, id);
    }

    @Override
    public T decode(ByteBuf buffer) {
        int id = ByteBufUtils.readVarInt(buffer);
        return map.byId(id);
    }
}
