package com.teamresourceful.resourcefullib.common.yabn.base.primitives;

import com.google.common.primitives.Ints;
import com.teamresourceful.resourcefullib.common.yabn.base.YabnType;

public record IntContents(int value) implements PrimitiveContents {

    @Override
    public YabnType getId() {
        return YabnType.INT;
    }

    @Override
    public byte[] toData() {
        return Ints.toByteArray(value);
    }
}
