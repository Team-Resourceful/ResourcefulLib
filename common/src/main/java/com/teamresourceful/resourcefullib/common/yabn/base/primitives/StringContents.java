package com.teamresourceful.resourcefullib.common.yabn.base.primitives;

import com.teamresourceful.resourcefullib.common.yabn.base.YabnType;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.charset.StandardCharsets;

public record StringContents(String value) implements PrimitiveContents {

    /**
     * The reason to force nonnull non-empty strings is it should be on the developer implementing
     * it to understand this can easily be an optional on their end or can be replaced by YABN null.
     */
    public StringContents {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("StringContents cannot be null or blank");
        }
    }

    @Override
    public YabnType getId() {
        return YabnType.STRING;
    }

    @Override
    public byte[] toData() {
        return ArrayUtils.add(value.getBytes(StandardCharsets.UTF_8), (byte) 0x00);
    }
}
