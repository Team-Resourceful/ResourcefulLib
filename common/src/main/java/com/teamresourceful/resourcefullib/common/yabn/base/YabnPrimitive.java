package com.teamresourceful.resourcefullib.common.yabn.base;

import com.teamresourceful.resourcefullib.common.yabn.base.primitives.*;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

public record YabnPrimitive(PrimitiveContents contents) implements YabnElement {

    public static YabnPrimitive ofString(String string) {
        return new YabnPrimitive(new StringContents(string));
    }

    public static YabnPrimitive ofBoolean(boolean bool) {
        return new YabnPrimitive(new BooleanContents(bool));
    }

    public static YabnPrimitive ofFloat(float f) {
        return new YabnPrimitive(new FloatContents(f));
    }

    public static YabnPrimitive ofDouble(double d) {
        return new YabnPrimitive(new DoubleContents(d));
    }

    public static YabnPrimitive ofByte(byte b) {
        return new YabnPrimitive(new ByteContents(b));
    }

    public static YabnPrimitive ofShort(short s) {
        return new YabnPrimitive(new ShortContents(s));
    }

    public static YabnPrimitive ofInt(int i) {
        return new YabnPrimitive(new IntContents(i));
    }

    public static YabnPrimitive ofLong(long l) {
        return new YabnPrimitive(new LongContents(l));
    }

    @Override
    public byte[] toData(@Nullable String key) {
        if (key == null) return toData();
        byte[] data = contents.toData();
        if (data.length == 0) {
            return YabnElement.key(contents.getId().id, key);
        }
        return ArrayUtils.addAll(YabnElement.key(contents.getId().id, key), data);
    }

    @Override
    public byte[] toData() {
        byte[] data = contents.toData();
        if (data.length == 0) {
            return new byte[]{contents.getId().id};
        }
        return ArrayUtils.addAll(new byte[]{contents.getId().id}, data);
    }
}
