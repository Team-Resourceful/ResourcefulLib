package com.teamresourceful.resourcefullib.common.codecs.yabn;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.RecordBuilder;
import com.teamresourceful.resourcefullib.common.yabn.base.YabnElement;
import com.teamresourceful.resourcefullib.common.yabn.base.YabnObject;

public class YabnRecordBuilder extends RecordBuilder.AbstractStringBuilder<YabnElement, YabnObject> {

    protected YabnRecordBuilder(DynamicOps<YabnElement> ops) {
        super(ops);
    }

    @Override
    protected YabnObject initBuilder() {
        return new YabnObject();
    }

    @Override
    protected YabnObject append(final String key, final YabnElement value, final YabnObject builder) {
        builder.put(key, value);
        return builder;
    }

    @Override
    protected DataResult<YabnElement> build(final YabnObject builder, final YabnElement prefix) {
        if (prefix == null || prefix.isNull()) return DataResult.success(builder);
        if (prefix instanceof YabnObject object) {
            final YabnObject result = new YabnObject();
            object.elements().forEach(result::put);
            builder.elements().forEach(result::put);
            return DataResult.success(result);
        }
        return DataResult.error("mergeToMap called with not a map: " + prefix, prefix);
    }
}
