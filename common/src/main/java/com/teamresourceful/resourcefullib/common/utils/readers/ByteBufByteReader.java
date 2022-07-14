package com.teamresourceful.resourcefullib.common.utils.readers;

import io.netty.buffer.ByteBuf;

public class ByteBufByteReader implements ByteReader {

    protected final ByteBuf buf;

    public ByteBufByteReader(ByteBuf buf) {
        this.buf = buf;
    }

    @Override
    public byte peek() {
        return buf.getByte(buf.readerIndex());
    }

    @Override
    public void advance() {
        buf.skipBytes(1);
    }

    @Override
    public byte readByte() {
        return buf.readByte();
    }
}
