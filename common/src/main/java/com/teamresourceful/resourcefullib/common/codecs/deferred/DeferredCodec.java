package com.teamresourceful.resourcefullib.common.codecs.deferred;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class DeferredCodec<E> implements Codec<Deferred<E>> {

    private final Codec<E> codec;

    public DeferredCodec(Codec<E> codec) {
        this.codec = codec;
    }

    @Override
    public <T> DataResult<Pair<Deferred<E>, T>> decode(DynamicOps<T> ops, T input) {
        return DataResult.success(Pair.of(Deferred.of(ops, codec, input), input));
    }

    @Override
    public <T> DataResult<T> encode(Deferred<E> input, DynamicOps<T> ops, T prefix) {
        if (input.get() == null) return DataResult.success(prefix);
        return codec.encode(input.get(), ops, prefix);
    }
}
