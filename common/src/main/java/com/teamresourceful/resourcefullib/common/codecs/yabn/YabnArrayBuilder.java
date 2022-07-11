package com.teamresourceful.resourcefullib.common.codecs.yabn;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.ListBuilder;
import com.teamresourceful.resourcefullib.common.yabn.base.YabnArray;
import com.teamresourceful.resourcefullib.common.yabn.base.YabnElement;

import java.util.function.UnaryOperator;

public class YabnArrayBuilder implements ListBuilder<YabnElement> {
    private DataResult<YabnArray> builder = DataResult.success(new YabnArray(), Lifecycle.stable());

    @Override
    public DynamicOps<YabnElement> ops() {
        return YabnOps.INSTANCE;
    }

    @Override
    public ListBuilder<YabnElement> add(final YabnElement value) {
        builder = builder.map(b -> b.add(value));
        return this;
    }

    @Override
    public ListBuilder<YabnElement> add(final DataResult<YabnElement> value) {
        builder = builder.apply2stable(YabnArray::add, value);
        return this;
    }

    @Override
    public ListBuilder<YabnElement> withErrorsFrom(final DataResult<?> result) {
        builder = builder.flatMap(r -> result.map(v -> r));
        return this;
    }

    @Override
    public ListBuilder<YabnElement> mapError(final UnaryOperator<String> onError) {
        builder = builder.mapError(onError);
        return this;
    }

    @Override
    public DataResult<YabnElement> build(final YabnElement prefix) {
        final DataResult<YabnElement> result = builder.flatMap(b -> {
            if (!(prefix instanceof YabnArray) && prefix != ops().empty()) {
                return DataResult.error("Cannot append a list to not a list: " + prefix, prefix);
            }

            final YabnArray array = new YabnArray();
            if (prefix instanceof YabnArray yabnArray) {
                array.elements().addAll(yabnArray.elements());
            }
            array.elements().addAll(b.elements());
            return DataResult.success(array, Lifecycle.stable());
        });

        builder = DataResult.success(new YabnArray(), Lifecycle.stable());
        return result;
    }
}