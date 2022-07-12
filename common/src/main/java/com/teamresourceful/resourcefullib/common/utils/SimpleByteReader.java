package com.teamresourceful.resourcefullib.common.utils;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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
        short value = Shorts.fromByteArray(Arrays.copyOfRange(data, index, index+2));
        index += 2;
        return value;
    }

    public int readInt() {
        int value = Ints.fromByteArray(Arrays.copyOfRange(data, index, index+4));
        index += 4;
        return value;
    }

    public long readLong() {
        long value = Longs.fromByteArray(Arrays.copyOfRange(data, index, index+8));
        index += 8;
        return value;
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

    public String readString() {
        byte[] stringData = new byte[0];
        while (peek() != 0x00) {
            stringData = ArrayUtils.add(stringData, readByte());
        }
        readByte(); // skip 0x00
        return new String(stringData, StandardCharsets.UTF_8);
    }

}
