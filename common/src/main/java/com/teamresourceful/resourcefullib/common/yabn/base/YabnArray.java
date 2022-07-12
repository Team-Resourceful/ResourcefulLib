package com.teamresourceful.resourcefullib.common.yabn.base;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record YabnArray(List<YabnElement> elements) implements YabnElement {

    public YabnArray() {
        this(new ArrayList<>());
    }

    public YabnArray add(YabnElement element) {
        elements.add(element);
        return this;
    }

    @Override
    public byte[] toData(@Nullable String key) {
        if (key == null) return toData();
        if (elements.isEmpty()) return YabnElement.key(YabnType.EMPTY_ARRAY.id, key);
        return ArrayUtils.addAll(YabnElement.key(YabnType.ARRAY.id, key), internalData());
    }

    @Override
    public byte[] toData() {
        if (elements.isEmpty()) return new byte[]{YabnType.EMPTY_ARRAY.id};
        return ArrayUtils.addAll(new byte[]{YabnType.ARRAY.id}, internalData());
    }

    private byte[] internalData() {
        byte[] data = new byte[0];
        for (YabnElement element : elements) {
            data = ArrayUtils.addAll(data, element.toData());
        }
        return ArrayUtils.addAll(data, YabnElement.EOD);
    }
}
