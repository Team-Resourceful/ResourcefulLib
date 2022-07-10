package com.teamresourceful.resourcefullib.common.yabn.base;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public record YabnObject(Map<String, YabnElement> elements) implements YabnElement {

    public YabnObject() {
        this(new LinkedHashMap<>());
    }

    public YabnObject put(String key, YabnElement element) {
        elements.put(key, element);
        return this;
    }

    public YabnElement get(String key) {
        return elements.get(key);
    }

    @Override
    public byte[] toData(@Nullable String key) {
        if (key == null) toData();
        return ArrayUtils.addAll(YabnElement.key(YabnType.OBJECT.id, key), internalData());
    }

    @Override
    public byte[] toData() {
        return ArrayUtils.addAll(new byte[]{YabnType.OBJECT.id}, internalData());
    }

    private byte[] internalData() {
        byte[] data = new byte[0];
        for (var entry : this.elements.entrySet()) {
            data = ArrayUtils.addAll(data, entry.getValue().toData(entry.getKey()));
        }
        return ArrayUtils.addAll(data, YabnElement.EOD);
    }
}
