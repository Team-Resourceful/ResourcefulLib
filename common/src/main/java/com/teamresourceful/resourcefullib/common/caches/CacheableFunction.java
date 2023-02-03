package com.teamresourceful.resourcefullib.common.caches;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class CacheableFunction<T, R> implements Function<T, R>, CachingFunction {

    private final Map<T, R> cache;
    private final Function<T, R> function;

    public CacheableFunction(Function<T, R> function, Map<T, R> cache) {
        this.cache = cache;
        this.function = function;
    }

    public CacheableFunction(Function<T, R> function) {
        this(function, new HashMap<>());
    }

    @Override
    public R apply(T t) {
        return this.cache.computeIfAbsent(t, function);
    }

    public void clear() {
        this.cache.clear();
    }

    public static <T, R> CacheableFunction<T, R> concurrent(Function<T, R> function) {
        return new CacheableFunction<>(function, new ConcurrentHashMap<>());
    }
}
