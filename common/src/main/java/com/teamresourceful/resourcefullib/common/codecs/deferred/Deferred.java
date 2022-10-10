package com.teamresourceful.resourcefullib.common.codecs.deferred;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

@ApiStatus.Experimental
public class Deferred<E> implements Supplier<E> {

    private DeferredDecoder<E, ?> decoder;
    private E value;

    private Deferred(DeferredDecoder<E, ?> decoder) {
        this.decoder = decoder;
    }

    public E get() {
        if (this.value == null) {
            this.value = decoder.decode();
            this.decoder = null; //Remove reference it's no longer needed.
        }
        return this.value;
    }

    public static <V, T> Deferred<V> of(DynamicOps<T> ops, Codec<V> codec, T input) {
        return new Deferred<>(new DeferredDecoder<>(ops, codec, input));
    }

    private record DeferredDecoder<E, T>(DynamicOps<T> ops, Codec<E> codec, T input) {

        public E decode() {
            return codec.decode(ops, input)
                    .result()
                    .map(Pair::getFirst)
                    .orElseThrow(() -> new IllegalStateException("Unable to parse deferred value"));
        }
    }

}
