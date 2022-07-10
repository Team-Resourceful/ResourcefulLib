package com.teamresourceful.resourcefullib.common.yabn.base.primitives;

import com.google.common.primitives.Longs;
import com.teamresourceful.resourcefullib.common.yabn.base.YabnType;

public record LongContents(long value) implements NumberPrimitiveContents {

    @Override
    public YabnType getId() {
        return YabnType.LONG;
    }

    @Override
    public byte[] toData() {
        return Longs.toByteArray(value);
    }

    @Override
    public Number getValue() {
        return value;
    }
}
