package com.teamresourceful.resourcefullib.common.utils.readers;

public class ArrayByteReader implements ByteReader {

    protected final byte[] data;
    protected int index = 0;

    public ArrayByteReader(byte[] data) {
        this.data = data;
    }

    public byte peek() {
        return data[index];
    }

    public void advance() {
        index++;
    }

    public byte readByte() {
        return data[index++];
    }

}
