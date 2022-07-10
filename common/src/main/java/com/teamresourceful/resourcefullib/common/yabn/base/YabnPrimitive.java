package com.teamresourceful.resourcefullib.common.yabn.base;

import com.teamresourceful.resourcefullib.common.yabn.base.primitives.PrimitiveContents;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

public record YabnPrimitive(PrimitiveContents contents) implements YabnElement {

    @Override
    public byte[] toData(@Nullable String key) {
        if (key == null) toData();
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
