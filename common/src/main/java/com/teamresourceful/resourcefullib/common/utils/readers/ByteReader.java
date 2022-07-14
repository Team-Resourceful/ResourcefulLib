package com.teamresourceful.resourcefullib.common.utils.readers;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;

public interface ByteReader {

    /**
     * @return returns the current byte in data without advancing the index.
     */
    byte peek();

    /**
     * Advances the index by 1.
     */
    void advance();

    /**
     * @return returns the current byte in data and advances the index.
     */
    byte readByte();

    default char readChar() {
        return (char) readByte();
    }

    default boolean readBoolean() {
        return readByte() == 0x01;
    }

    default short readShort() {
        return Shorts.fromBytes(readByte(), readByte());
    }

    default int readInt() {
        return Ints.fromBytes(readByte(), readByte(), readByte(), readByte());
    }

    default long readLong() {
        return Longs.fromBytes(readByte(), readByte(), readByte(), readByte(), readByte(), readByte(), readByte(), readByte());
    }

    default float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    default double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    default String readString() {
        StringBuilder builder = new StringBuilder();
        while (peek() != 0) {
            builder.append(readChar());
        }
        advance();
        return builder.toString();
    }
}
