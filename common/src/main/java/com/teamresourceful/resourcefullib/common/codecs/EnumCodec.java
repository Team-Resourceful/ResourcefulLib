package com.teamresourceful.resourcefullib.common.codecs;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Function;

public final class EnumCodec<T extends Enum<T>> implements Codec<T> {

    private final Codec<T> codec;

    private EnumCodec(Codec<T> codec) {
        this.codec = codec;
    }

    public static <T extends Enum<T>> EnumCodec<T> of(Class<T> clazz) {
        return new EnumCodec<>(intOrConstCodec(clazz));
    }

    public static <T extends Enum<T> & StringRepresentable> EnumCodec<T> ofRepresentable(Class<T> clazz, Function<String, @Nullable T> getter) {
        return new EnumCodec<>(intOrConstOrRepresentableCodec(clazz, getter));
    }

    @Override
    public <T1> DataResult<Pair<T, T1>> decode(DynamicOps<T1> ops, T1 input) {
        return codec.decode(ops, input);
    }

    @Override
    public <T1> DataResult<T1> encode(T input, DynamicOps<T1> ops, T1 prefix) {
        return codec.encode(input, ops, prefix);
    }

    private static <T extends Enum<T>> Codec<T> intOrConstCodec(Class<T> enumClass) {
        return CodecExtras.eitherLeft(Codec.either(EnumCodec.constCodec(enumClass), EnumCodec.intCodec(enumClass)));
    }

    private static <T extends Enum<T> & StringRepresentable> Codec<T> intOrConstOrRepresentableCodec(Class<T> enumClass, Function<String, T> getter) {
        return CodecExtras.eitherLeft(Codec.either(representableCodec(getter), intOrConstCodec(enumClass)));
    }

    private static <T extends Enum<T>> Codec<T> intCodec(Class<T> enumClass) {
        return Codec.INT.flatXmap(
            ordinal -> {
                T[] values = enumClass.getEnumConstants();
                if (ordinal >= 0 && ordinal < values.length) {
                    return DataResult.success(values[ordinal]);
                }
                return DataResult.error("Unknown enum ordinal: " + ordinal);
            }, value -> DataResult.success(value.ordinal())
        );
    }

    private static <T extends Enum<T>> Codec<T> constCodec(Class<T> enumClass) {
        return Codec.STRING.flatXmap(id -> {
            try {
                return DataResult.success(Enum.valueOf(enumClass, id.toLowerCase(Locale.ROOT)));
            } catch (Exception e) {
                return DataResult.error("Unknown type: " + id);
            }
        }, value -> DataResult.success(value.name()));
    }

    private static <T extends Enum<T> & StringRepresentable> Codec<T> representableCodec(Function<String, @Nullable T> getter) {
        return Codec.STRING.flatXmap(id -> {
            T value = getter.apply(id);
            return value != null ? DataResult.success(value) : DataResult.error("Unknown type: " + id);
        }, value -> DataResult.success(value.getSerializedName()));
    }
}
