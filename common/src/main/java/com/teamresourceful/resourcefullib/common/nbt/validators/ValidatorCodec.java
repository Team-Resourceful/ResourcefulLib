package com.teamresourceful.resourcefullib.common.nbt.validators;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.function.Consumer;

@ApiStatus.Internal
public final class ValidatorCodec<I extends Tag> implements Codec<Validator<I>> {

    private final Codec<Validator<I>> codec;

    @SuppressWarnings("unchecked")
    public ValidatorCodec(Codec<? extends Validator<I>> defaultCodec, Consumer<AddCallback<I>> consumer) {
        ImmutableMap.Builder<String, Codec<Validator<I>>> builder = ImmutableMap.builder();
        consumer.accept((key, codec) -> builder.put(key, (Codec<Validator<I>>) codec));
        Map<String, Codec<Validator<I>>> validators = builder.build();
        this.codec = CodecExtras.eitherRight(Codec.either(
            (Codec<Validator<I>>) defaultCodec,
            Codec.STRING.dispatch(Validator::id, validators::get)
        ));
    }


    @Override
    public <T> DataResult<Pair<Validator<I>, T>> decode(DynamicOps<T> ops, T input) {
        return this.codec.decode(ops, input);
    }

    @Override
    public <T> DataResult<T> encode(Validator<I> input, DynamicOps<T> ops, T prefix) {
        return this.codec.encode(input, ops, prefix);
    }

    @FunctionalInterface
    public interface AddCallback<T extends Tag> {
        void add(String key, Codec<? extends Validator<T>> codec);
    }
}
