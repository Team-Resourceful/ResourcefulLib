package com.teamresourceful.resourcefullib.common.codecs;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.teamresourceful.resourcefullib.common.collections.WeightedCollection;
import com.teamresourceful.resourcefullib.common.utils.RandomCollection;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

public final class CodecExtras {

    /**
     * @deprecated Use {@link #weightedCollection(Codec, ToDoubleFunction) } instead
     */
    @Deprecated(since = "1.20", forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "1.20")
    public static <T> Codec<RandomCollection<T>> randomCollection(Codec<T> codec, ToDoubleFunction<T> weighter) {
        return codec.listOf().xmap(set -> RandomCollection.of(set, weighter), collection -> collection.stream().toList());
    }

    public static <T> Codec<WeightedCollection<T>> weightedCollection(Codec<T> codec, ToDoubleFunction<T> weighter) {
        return codec.listOf().xmap(set -> WeightedCollection.of(set, weighter), collection -> collection.stream().toList());
    }

    public static <T> Codec<Set<T>> set(Codec<T> codec) {
        return codec.listOf().xmap(HashSet::new, ArrayList::new);
    }

    public static <T> Codec<Set<T>> linkedSet(Codec<T> codec) {
        return codec.listOf().xmap(LinkedHashSet::new, LinkedList::new);
    }

    public static <T> Codec<T> passthrough(Function<T, JsonElement> encoder, Function<JsonElement, T> decoder) {
        return Codec.PASSTHROUGH.comapFlatMap(dynamic -> decoder(dynamic, decoder), item -> encoder(item, encoder));
    }

    private static <T> DataResult<T> decoder(Dynamic<?> dynamic, Function<JsonElement, T> decoder) {
        if (dynamic.getValue() instanceof JsonElement jsonElement) {
            return DataResult.success(decoder.apply(jsonElement));
        }
        return DataResult.error("Value was not an instance of JsonElement");
    }

    private static <T> Dynamic<JsonElement> encoder(T input, Function<T, JsonElement> encoder) {
        return new Dynamic<>(JsonOps.INSTANCE, encoder.apply(input));
    }
}
