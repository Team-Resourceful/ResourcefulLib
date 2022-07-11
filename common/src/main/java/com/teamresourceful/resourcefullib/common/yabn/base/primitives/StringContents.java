package com.teamresourceful.resourcefullib.common.yabn.base.primitives;

import com.teamresourceful.resourcefullib.common.yabn.base.YabnType;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.charset.StandardCharsets;

public record StringContents(String value) implements PrimitiveContents {

    public StringContents {
        if (value.contains("\u0000")) {
            throw new IllegalArgumentException("String cannot contain null character.");
        }
    }

    @Override
    public YabnType getId() {
        return value.isEmpty() ? YabnType.STRING_EMPTY : YabnType.STRING;
    }

    @Override
    public byte[] toData() {
        if (value.isEmpty()) return new byte[0];
        return ArrayUtils.add(value.getBytes(StandardCharsets.UTF_8), (byte) 0x00);
    }
}
