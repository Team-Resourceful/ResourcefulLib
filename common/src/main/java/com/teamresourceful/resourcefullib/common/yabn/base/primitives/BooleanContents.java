package com.teamresourceful.resourcefullib.common.yabn.base.primitives;

import com.teamresourceful.resourcefullib.common.yabn.base.YabnType;

public record BooleanContents(boolean value) implements PrimitiveContents {

    @Override
    public YabnType getId() {
        return value ? YabnType.BOOLEAN_TRUE : YabnType.BOOLEAN_FALSE;
    }

    @Override
    public byte[] toData() {
        return new byte[0];
    }
}
