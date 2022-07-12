package com.teamresourceful.resourcefullib.common.utils;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;

public class SimpleByteReader {

    protected final byte[] data;
    protected int index = 0;

    public SimpleByteReader(byte[] data) {
        this.data = data;
    }

    /**
     * Gives the next byte in data without advancing the index.
     */
    public byte peek() {
        return data[index];
    }

    public byte readByte() {
        return data[index++];
    }

    public short readShort() {
        return Shorts.fromBytes(readByte(), readByte());
    }

    public int readInt() {
        return Ints.fromBytes(readByte(), readByte(), readByte(), readByte());
    }

    public long readLong() {
        return Longs.fromBytes(readByte(), readByte(), readByte(), readByte(), readByte(), readByte(), readByte(), readByte());
    }

    public boolean readBoolean() {
        return readByte() == 0x01;
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public char readChar() {
        return (char) readByte();
    }

    public String readString() {
        StringBuilder builder = new StringBuilder();
        while (peek() != 0x00) {
            builder.append(readChar());
        }
        readByte(); // skip 0x00
        return builder.toString();
    }

}
