package com.teamresourceful.resourcefullib.common.codecs.bounds;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;

import java.util.function.Function;

public final class UniformedNumberCodecs {

    private UniformedNumberCodecs() throws UtilityClassException {
        throw new UtilityClassException();
    }

    public static final Codec<UniformFloat> FLOAT_CODEC = getFloatCodec();
    public static final Codec<UniformInt> INT_CODEC = getIntCodec();

    private static Codec<UniformFloat> getFloatCodec() {
        Codec<UniformFloat> codec = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("min").forGetter(UniformFloat::min),
                Codec.FLOAT.fieldOf("max").forGetter(UniformFloat::max)
        ).apply(instance, UniformFloat::of));
        return codec.comapFlatMap(uniformFloat -> {
            if (uniformFloat.max() < uniformFloat.min()) {
                return DataResult.error(() -> "Max must be at least min, min: " + uniformFloat.min()+ ", max: " + uniformFloat.max());
            }
            return DataResult.success(uniformFloat);
        }, Function.identity());
    }

    public static Codec<UniformInt> getIntCodec() {
        Codec<UniformInt> codec = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("min").forGetter(UniformInt::minInclusive),
                Codec.INT.fieldOf("max").forGetter(UniformInt::maxInclusive)
        ).apply(instance, UniformInt::of));
        return codec.comapFlatMap(uniformInt -> {
            if (uniformInt.maxInclusive() < uniformInt.minInclusive()) {
                return DataResult.error(() -> "Max must be at least min, min: " + uniformInt.minInclusive() + ", max: " + uniformInt.maxInclusive());
            }
            return DataResult.success(uniformInt);
        }, Function.identity());
    }

    public static Codec<UniformInt> rangedUniformIntCodec(int min, int max) {
        Codec<UniformInt> codec = RecordCodecBuilder.create(instance -> instance.group(
                Codec.intRange(min, max).fieldOf("min").forGetter(UniformInt::minInclusive),
                Codec.intRange(min, max).fieldOf("max").forGetter(UniformInt::maxInclusive)
        ).apply(instance, UniformInt::of));
        return codec.comapFlatMap(uniformInt -> {
            if (uniformInt.maxInclusive() < uniformInt.minInclusive()) {
                return DataResult.error(() -> "Max must be at least min, min: " + uniformInt.minInclusive() + ", max: " + uniformInt.maxInclusive());
            }
            return DataResult.success(uniformInt);
        }, Function.identity());
    }

    public static Codec<UniformFloat> rangedUniformFloatCodec(float min, float max) {
        Codec<UniformFloat> codec = RecordCodecBuilder.create(instance -> instance.group(
                Codec.floatRange(min, max).fieldOf("min").forGetter(UniformFloat::min),
                Codec.floatRange(min, max).fieldOf("max").forGetter(UniformFloat::max)
        ).apply(instance, UniformFloat::of));
        return codec.comapFlatMap(uniformFloat -> {
            if (uniformFloat.max() < uniformFloat.min()) {
                return DataResult.error(() -> "Max must be at least min, min: " + uniformFloat.min()+ ", max: " + uniformFloat.max());
            }
            return DataResult.success(uniformFloat);
        }, Function.identity());
    }
}
