package com.teamresourceful.resourcefullib.common.codecs.predicates.properties;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Optional;

public interface PropertyMatcher {

    Codec<PropertyMatcher> CODEC = Codec.either(ExactPropertyMatcher.CODEC, RangePropertyMatcher.CODEC)
            .flatXmap(
                either -> DataResult.success(either.map(p -> p, p -> p)),
                matcher -> {
                    if (matcher instanceof ExactPropertyMatcher) {
                        return DataResult.success(Either.left((ExactPropertyMatcher) matcher));
                    } else if (matcher instanceof RangePropertyMatcher) {
                        return DataResult.success(Either.right((RangePropertyMatcher) matcher));
                    }
                    return DataResult.error("Unknown PropertyMatcher type");
                }
            );

    default  <S extends StateHolder<?, S>> boolean match(String name, StateDefinition<?, S> definition, S holder) {
        Property<?> property = definition.getProperty(name);
        return property != null && match(holder, property);
    }

    <T extends Comparable<T>> boolean match(StateHolder<?, ?> holder, Property<T> property);

    Codec<? extends PropertyMatcher> codec();

    Optional<String> value(RandomSource source);

}
