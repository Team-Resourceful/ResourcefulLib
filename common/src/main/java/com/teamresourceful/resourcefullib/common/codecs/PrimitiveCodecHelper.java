package com.teamresourceful.resourcefullib.common.codecs;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;

public final class PrimitiveCodecHelper {

    private PrimitiveCodecHelper() throws UtilityClassException {
        throw new UtilityClassException();
    }

    public static <T> PrimitiveCodec<T> create(
            Reader<T> reader,
            Writer<T> writer,
            String name
            ) {
        return new PrimitiveCodec<>() {
            @Override
            public <I> DataResult<T> read(final DynamicOps<I> ops, final I input) {
                return reader.read(ops, input);
            }

            @Override
            public <I> I write(final DynamicOps<I> ops, final T value) {
                return writer.write(ops, value);
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    @FunctionalInterface
    public interface Reader<T> {
        <I> DataResult<T> read(final DynamicOps<I> ops, final I input);
    }

    @FunctionalInterface
    public interface Writer<T> {
        <I> I write(final DynamicOps<I> ops, final T value);
    }
}
