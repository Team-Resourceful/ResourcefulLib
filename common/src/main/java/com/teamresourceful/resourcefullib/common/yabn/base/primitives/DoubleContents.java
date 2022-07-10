package com.teamresourceful.resourcefullib.common.yabn.base.primitives;

import com.google.common.primitives.Longs;
import com.teamresourceful.resourcefullib.common.yabn.base.YabnType;

public record DoubleContents(double value) implements NumberPrimitiveContents {

    @Override
    public YabnType getId() {
        return YabnType.DOUBLE;
    }

    @Override
    public byte[] toData() {
        return Longs.toByteArray(Double.doubleToLongBits(value));
    }

    @Override
    public Number getValue() {
        return value;
    }
}
