package com.teamresourceful.resourcefullib.common.yabn.base.primitives;

import com.google.common.primitives.Ints;
import com.teamresourceful.resourcefullib.common.yabn.base.YabnType;

public record FloatContents(float value) implements NumberPrimitiveContents {

    @Override
    public YabnType getId() {
        return YabnType.FLOAT;
    }

    @Override
    public byte[] toData() {
        return Ints.toByteArray(Float.floatToIntBits(value));
    }

    @Override
    public Number getValue() {
        return value;
    }
}
