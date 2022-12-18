package com.teamresourceful.resourcefullib.common.codecs.predicates.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Optional;

public record RangePropertyMatcher(Optional<String> min, Optional<String> max) implements PropertyMatcher {

    public static final Codec<RangePropertyMatcher> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("min").forGetter(RangePropertyMatcher::min),
            Codec.STRING.optionalFieldOf("max").forGetter(RangePropertyMatcher::max)
    ).apply(instance, RangePropertyMatcher::new));

    @Override
    public <T extends Comparable<T>> boolean match(StateHolder<?, ?> holder, Property<T> property) {
        T comparable = holder.getValue(property);
        if (this.min.isPresent()) {
            Optional<T> optional = property.getValue(this.min.get());
            if (optional.isEmpty() || comparable.compareTo(optional.get()) < 0) {
                return false;
            }
        }

        if (this.max.isPresent()) {
            Optional<T> optional = property.getValue(this.max.get());
            if (optional.isEmpty() || comparable.compareTo(optional.get()) > 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Codec<RangePropertyMatcher> codec() {
        return CODEC;
    }

    @Override
    public Optional<String> value(RandomSource source) {
        if (this.max.isPresent() && this.min.isPresent()) {
            return source.nextBoolean() ? this.min : this.max;
        } else if (this.max.isPresent()) {
            return this.max;
        } else if (this.min.isPresent()) {
            return this.min;
        }
        return Optional.empty();
    }
}
