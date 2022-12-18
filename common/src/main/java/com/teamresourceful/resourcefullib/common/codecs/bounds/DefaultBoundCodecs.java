package com.teamresourceful.resourcefullib.common.codecs.bounds;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import net.minecraft.advancements.critereon.MinMaxBounds;

@SuppressWarnings("ConstantConditions")
public final class DefaultBoundCodecs {

    private DefaultBoundCodecs() throws UtilityClassException {
        throw new UtilityClassException();
    }

    public static final BoundCodec<Integer, MinMaxBounds.Ints> INT = new BoundCodec<>(Codec.INT, MinMaxBounds.Ints::between);
    public static final BoundCodec<Double, MinMaxBounds.Doubles> DOUBLE = new BoundCodec<>(Codec.DOUBLE, MinMaxBounds.Doubles::between);

}
