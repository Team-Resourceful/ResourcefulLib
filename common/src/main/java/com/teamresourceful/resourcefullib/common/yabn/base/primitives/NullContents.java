package com.teamresourceful.resourcefullib.common.yabn.base.primitives;

import com.teamresourceful.resourcefullib.common.yabn.base.YabnPrimitive;
import com.teamresourceful.resourcefullib.common.yabn.base.YabnType;

public class NullContents implements PrimitiveContents {

    private static final NullContents INSTANCE = new NullContents();
    public static final YabnPrimitive NULL = new YabnPrimitive(INSTANCE);

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
