package com.teamresourceful.resourcefullib.common.nbt.validators;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.teamresourceful.resourcefullib.common.nbt.validators.list.ListValidator;
import com.teamresourceful.resourcefullib.common.nbt.validators.numeric.NumericValidator;
import com.teamresourceful.resourcefullib.common.nbt.validators.object.ObjectValidator;
import com.teamresourceful.resourcefullib.common.nbt.validators.string.StringValidator;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class FullValidatorCodec implements Codec<Validator<?>> {

    public static final Codec<Validator<?>> CODEC = new FullValidatorCodec();

    private FullValidatorCodec() {}

    @Override
    @SuppressWarnings("unchecked")
    public <T> DataResult<Pair<Validator<?>, T>> decode(DynamicOps<T> ops, T input) {
        return (DataResult<Pair<Validator<?>, T>>) decode(ops, input,
            ListValidator.CODEC,
            NumericValidator.CODEC,
            ObjectValidator.CODEC,
            StringValidator.CODEC
        );
    }

    @Override
    public <T> DataResult<T> encode(Validator<?> input, DynamicOps<T> ops, T prefix) {
        if (input instanceof ListValidator listValidator) {
            return ListValidator.CODEC.encode(listValidator, ops, prefix);
        } else if (input instanceof NumericValidator numericValidator) {
            return NumericValidator.CODEC.encode(numericValidator, ops, prefix);
        } else if (input instanceof ObjectValidator objectValidator) {
            return ObjectValidator.CODEC.encode(objectValidator, ops, prefix);
        } else if (input instanceof StringValidator stringValidator) {
            return StringValidator.CODEC.encode(stringValidator, ops, prefix);
        }
        return DataResult.error(() -> "Failed to encode validator.");
    }

    @SafeVarargs
    private static <T> DataResult<? extends Pair<? extends Validator<?>, T>> decode(DynamicOps<T> ops, T input, Codec<? extends Validator<?>>... codecs) {
        for (Codec<? extends Validator<?>> codec : codecs) {
            var result = codec.decode(ops, input);
            if (result.result().isPresent()) {
                return result;
            }
        }
        return DataResult.error(() -> "Failed to decode validator.");
    }
}
