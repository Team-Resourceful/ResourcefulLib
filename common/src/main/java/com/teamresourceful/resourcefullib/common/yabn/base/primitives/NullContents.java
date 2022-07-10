package com.teamresourceful.resourcefullib.common.yabn.base.primitives;

import com.teamresourceful.resourcefullib.common.yabn.base.YabnType;

public class NullContents implements PrimitiveContents {

    public static final NullContents INSTANCE = new NullContents();

    private NullContents() {}

    @Override
    public YabnType getId() {
        return YabnType.NULL;
    }

    @Override
    public byte[] toData() {
        return new byte[0];
    }
}
