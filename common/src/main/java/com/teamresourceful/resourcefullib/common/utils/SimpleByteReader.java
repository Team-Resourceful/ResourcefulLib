package com.teamresourceful.resourcefullib.common.utils;

import com.teamresourceful.resourcefullib.common.utils.readers.ByteReader;
import org.jetbrains.annotations.ApiStatus;

/**
 * @deprecated This has been replaced by {@link com.teamresourceful.resourcefullib.common.utils.readers.ArrayByteReader}
 */
@Deprecated(since = "1.0.4", forRemoval = true)
@ApiStatus.ScheduledForRemoval(inVersion = "1.19.1")
public class SimpleByteReader implements ByteReader {

    protected final byte[] data;
    protected int index = 0;

    public SimpleByteReader(byte[] data) {
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
