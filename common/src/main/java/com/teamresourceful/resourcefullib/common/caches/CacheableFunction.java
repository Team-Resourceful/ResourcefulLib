package com.teamresourceful.resourcefullib.common.caches;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CacheableFunction<T, R> implements Function<T, R>, CachingFunction {

    private final Map<T, R> cache = new HashMap<>();
    private final Function<T, R> function;

    public CacheableFunction(Function<T, R> function) {
        this.function = function;
    }

    @Override
    public R apply(T t) {
        return this.cache.computeIfAbsent(t, function);
    }

    public void clear() {
        this.cache.clear();
    }

}
