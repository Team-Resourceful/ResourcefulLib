package com.teamresourceful.resourcefullib.common.utils;

import net.minecraft.network.FriendlyByteBuf;

/**
 * This class is used to wrap the vanilla {@link FriendlyByteBuf} class to be able to be used in a YABN parser.
 */
public class SimpleBufferByteReader extends SimpleByteReader {

    private final FriendlyByteBuf buf;

    public SimpleBufferByteReader(FriendlyByteBuf buf) {
        super(new byte[0]);
        this.buf = buf;
    }

    @Override
    public byte peek() {
        return buf.getByte(buf.readerIndex());
    }

    @Override
    public byte readByte() {
        return buf.readByte();
    }

    @Override
    public short readShort() {
        return buf.readShort();
    }

    @Override
    public int readInt() {
        return buf.readInt();
    }

    @Override
    public long readLong() {
        return buf.readLong();
    }

}
