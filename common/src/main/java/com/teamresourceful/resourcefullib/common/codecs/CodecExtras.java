package com.teamresourceful.resourcefullib.common.codecs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.teamresourceful.resourcefullib.common.collections.WeightedCollection;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import net.minecraft.core.Registry;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

public final class CodecExtras {

    public static final Codec<Double> NON_NEGATIVE_DOUBLE = doubleRangeWithMessage(0, Double.MAX_VALUE, aDouble -> "Value must be greater than or equal to 0. Found: " + aDouble);
    public static final Codec<Double> POSITIVE_DOUBLE = doubleRangeWithMessage(1, Double.MAX_VALUE, aDouble -> "Value must be greater than 0. Found: " + aDouble);
    public static final Codec<Double> NON_POSITIVE_DOUBLE = doubleRangeWithMessage(Double.MIN_VALUE, 0, aDouble -> "Value must be less than or equal to 0. Found: " + aDouble);
    public static final Codec<Double> NEGATIVE_DOUBLE = doubleRangeWithMessage(Double.MIN_VALUE, -1, aDouble -> "Value must be less than 0. Found: " + aDouble);
    public static final Codec<Double> DOUBLE_UNIT_INTERVAL = doubleRangeWithMessage(0, 1, aDouble -> "Value must be between 0 and 1. Found: " + aDouble);

    public static final Codec<Float> NON_NEGATIVE_FLOAT = floatRangeWithMessage(0f, Float.MAX_VALUE, aFloat -> "Value must be greater than or equal to 0. Found: " + aFloat);
    public static final Codec<Float> POSITIVE_FLOAT = floatRangeWithMessage(1f, Float.MAX_VALUE, aFloat -> "Value must be greater than 0. Found: " + aFloat);
    public static final Codec<Float> NON_POSITIVE_FLOAT = floatRangeWithMessage(Float.MIN_VALUE, 0f, aFloat -> "Value must be less than or equal to 0. Found: " + aFloat);
    public static final Codec<Float> NEGATIVE_FLOAT = floatRangeWithMessage(Float.MIN_VALUE, -1f, aFloat -> "Value must be less than 0. Found: " + aFloat);
    public static final Codec<Float> FLOAT_UNIT_INTERVAL = floatRangeWithMessage(0f, 1f, aFloat -> "Value must be between 0 and 1. Found: " + aFloat);

    public static final Codec<Integer> NON_NEGATIVE_INT = ExtraCodecs.NON_NEGATIVE_INT;
    public static final Codec<Integer> POSITIVE_INT = ExtraCodecs.POSITIVE_INT;
    public static final Codec<Integer> NON_POSITIVE_INT = intRangeWithMessage(Integer.MIN_VALUE, 0, integer -> "Value must be less than or equal to 0. Found: " + integer);
    public static final Codec<Integer> NEGATIVE_INT = intRangeWithMessage(Integer.MIN_VALUE, -1, integer -> "Value must be less than 0. Found: " + integer);
    public static final Codec<Integer> INT_UNIT_INTERVAL = intRangeWithMessage(0, 1, integer -> "Value must be between 0 and 1. Found: " + integer);

    private CodecExtras() throws UtilityClassException {
        throw new UtilityClassException();
    }

    public static final PrimitiveCodec<Number> NUMBER = PrimitiveCodecHelper.create(
            DynamicOps::getNumberValue, DynamicOps::createNumeric,
            "Number"
    );

    public static <O, A> Function<O, Optional<A>> optionalFor(final Function<O, @Nullable A> getter) {
        return o -> Optional.ofNullable(getter.apply(o));
    }
    public static <T> Codec<WeightedCollection<T>> weightedCollection(Codec<T> codec, ToDoubleFunction<T> weighter) {
        return codec.listOf().xmap(set -> WeightedCollection.of(set, weighter), collection -> collection.stream().toList());
    }

    public static <T> Codec<T> registryId(Registry<T> registry) {
        return Codec.INT.comapFlatMap(value -> {
            T t = registry.byId(value);
            if (t == null) {
                return DataResult.error(() -> "Unknown registry value: " + value);
            }
            return DataResult.success(t);
        }, registry::getId);
    }

    public static <T> Codec<Set<T>> set(Codec<T> codec) {
        return codec.listOf().xmap(HashSet::new, ArrayList::new);
    }

    public static <T> Codec<Set<T>> linkedSet(Codec<T> codec) {
        return codec.listOf().xmap(LinkedHashSet::new, LinkedList::new);
    }

    public static <T> Codec<T> passthrough(Function<T, JsonElement> encoder, Function<JsonElement, T> decoder) {
        return Codec.PASSTHROUGH.comapFlatMap(dynamic -> {
            if (dynamic.getValue() instanceof JsonElement jsonElement) {
                var output = clearNulls(jsonElement);
                if (output == null) {
                    return DataResult.error(() -> "Value was null for decoder: " + decoder);
                }
                return DataResult.success(decoder.apply(output));
            }
            return DataResult.error(() -> "Value was not an instance of JsonElement");
        }, input -> new Dynamic<>(JsonOps.INSTANCE, clearNulls(encoder.apply(input))));
    }

    public static <S> Codec<S> eitherRight(Codec<Either<S, S>> eitherCodec) {
        return eitherCodec.xmap(e -> e.map(p -> p, p -> p), Either::right);
    }

    public static <S> Codec<S> eitherLeft(Codec<Either<S, S>> eitherCodec) {
        return eitherCodec.xmap(e -> e.map(p -> p, p -> p), Either::left);
    }

    private static JsonElement clearNulls(JsonElement json) {
        if (json instanceof JsonObject object) {
            JsonObject newObject = new JsonObject();
            for (String key : object.keySet()) {
                JsonElement element = clearNulls(object.get(key));
                if (element != null) {
                    newObject.add(key, element);
                }
            }
            return newObject;
        } else if (json instanceof JsonArray array) {
            JsonArray newArray = new JsonArray();
            for (JsonElement item : array) {
                JsonElement element = clearNulls(item);
                if (element != null) {
                    newArray.add(element);
                }
            }
            return newArray;
        }
        return json.isJsonNull() ? null : json;
    }

    private static Codec<Integer> intRangeWithMessage(int i, int j, Function<Integer, String> function) {
        return ExtraCodecs.validate(
                Codec.INT,
                integer -> integer.compareTo(i) >= 0 && integer.compareTo(j) <= 0 ? DataResult.success(integer) : DataResult.error(() -> function.apply(integer))
        );
    }

    private static Codec<Double> doubleRangeWithMessage(double i, double j, Function<Double, String> function) {
        return ExtraCodecs.validate(
                Codec.DOUBLE,
                aDouble -> aDouble.compareTo(i) >= 0 && aDouble.compareTo(j) <= 0 ? DataResult.success(aDouble) : DataResult.error(() -> function.apply(aDouble))
        );
    }

    private static Codec<Float> floatRangeWithMessage(float i, float j, Function<Float, String> function) {
        return ExtraCodecs.validate(
                Codec.FLOAT,
                aFloat -> aFloat.compareTo(i) >= 0 && aFloat.compareTo(j) <= 0 ? DataResult.success(aFloat) : DataResult.error(() -> function.apply(aFloat))
        );
    }
}
