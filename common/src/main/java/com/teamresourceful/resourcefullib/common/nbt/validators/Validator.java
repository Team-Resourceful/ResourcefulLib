package com.teamresourceful.resourcefullib.common.nbt.validators;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.Tag;

import java.util.function.Predicate;

public interface Validator<T extends Tag> extends Predicate<T> {
    Codec<Validator<?>> CODEC = FullValidatorCodec.CODEC;

    String id();

    TagValidationType type();

    @SuppressWarnings("unchecked")
    default boolean testAndValidate(Tag tag) {
        return type().test(tag) && test((T) tag);
    }
}
