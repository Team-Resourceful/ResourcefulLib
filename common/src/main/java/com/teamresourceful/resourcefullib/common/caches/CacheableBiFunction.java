package com.teamresourceful.resourcefullib.common.caches;

import com.mojang.datafixers.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CacheableBiFunction<T, U, R> implements BiFunction<T, U, R>, CachingFunction {

    private final Map<Pair<T, U>, R> cache;
    private final Function<Pair<T, U>, R> function;

    public CacheableBiFunction(BiFunction<T, U, R> function, Map<Pair<T, U>, R> cache) {
        this.function = pair -> function.apply(pair.getFirst(), pair.getSecond());
        this.cache = cache;
    }

    public CacheableBiFunction(BiFunction<T, U, R> function) {
        this(function, new HashMap<>());
    }

    @Override
    public R apply(T t, U u) {
        return this.cache.computeIfAbsent(Pair.of(t, u), function);
    }

    public void clear() {
        this.cache.clear();
    }

    public static <T, U, R> CacheableBiFunction<T, U, R> concurrent(BiFunction<T, U, R> function) {
        return new CacheableBiFunction<>(function, new ConcurrentHashMap<>());
    }
}
