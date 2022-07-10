package com.teamresourceful.resourcefullib.common.yabn.base.primitives;

import com.google.common.primitives.Shorts;
import com.teamresourceful.resourcefullib.common.yabn.base.YabnType;

public record ShortContents(short value) implements NumberPrimitiveContents {

    @Override
    public YabnType getId() {
        return YabnType.SHORT;
    }

    @Override
    public byte[] toData() {
        return Shorts.toByteArray(value);
    }

    @Override
    public Number getValue() {
        return value;
    }
}
