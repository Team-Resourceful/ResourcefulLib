package com.teamresourceful.resourcefullib.common.yabn.base.primitives;

import com.teamresourceful.resourcefullib.common.yabn.base.YabnType;

public record ByteContents(byte value) implements NumberPrimitiveContents {

    @Override
    public YabnType getId() {
        return YabnType.BYTE;
    }

    @Override
    public byte[] toData() {
        return new byte[]{value};
    }

    @Override
    public Number getValue() {
        return value;
    }
}
