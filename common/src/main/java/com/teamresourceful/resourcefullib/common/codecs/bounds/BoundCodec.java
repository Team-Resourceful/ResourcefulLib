package com.teamresourceful.resourcefullib.common.codecs.bounds;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import net.minecraft.advancements.critereon.MinMaxBounds;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BoundCodec<T extends Number, B extends MinMaxBounds<T>> implements Codec<B> {

    private final Codec<B> codec;
    private final Codec<B> singleCodec;
    private final Codec<B> minMaxCodec;


    public BoundCodec(Codec<T> typeCodec, BoundsFactory<T, B> factory) {
        this.singleCodec = createSingle(typeCodec, factory);
        this.minMaxCodec = createMinMax(typeCodec, factory);
        this.codec = CodecExtras.eitherLeft(Codec.either(singleCodec, minMaxCodec));
    }

    @Override
    public <I> DataResult<Pair<B, I>> decode(DynamicOps<I> ops, I input) {
        return codec.decode(ops, input);
    }

    @Override
    public <I> DataResult<I> encode(B input, DynamicOps<I> ops, I prefix) {
        if (input.isAny()) {
            return DataResult.success(ops.empty());
        }
        if (input.getMin() != null && input.getMin().equals(input.getMax())) {
            return singleCodec.encode(input, ops, prefix);
        }
        return minMaxCodec.encode(input, ops, prefix);
    }

    private static <T extends Number, B extends MinMaxBounds<T>> Codec<B> createSingle(Codec<T> type, BoundsFactory<T, B> factory) {
        return type.comapFlatMap((value) -> DataResult.success(factory.create(value, value)), MinMaxBounds::getMin);
    }

    private static <T extends Number, B extends MinMaxBounds<T>> Codec<B> createMinMax(Codec<T> type, BoundsFactory<T, B> factory) {
        return RecordCodecBuilder.create(instance -> instance.group(
            type.optionalFieldOf("min").forGetter(bounds -> Optional.ofNullable(bounds.getMin())),
            type.optionalFieldOf("max").forGetter(bounds -> Optional.ofNullable(bounds.getMax()))
        ).apply(instance, (min, max) -> factory.create(min.orElse(null), max.orElse(null))));
    }

    @FunctionalInterface
    public interface BoundsFactory<T extends Number, R extends MinMaxBounds<T>> {
        R create(@Nullable T number, @Nullable T number2);
    }
}
