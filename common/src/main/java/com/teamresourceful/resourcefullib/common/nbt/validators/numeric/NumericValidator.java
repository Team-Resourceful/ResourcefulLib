package com.teamresourceful.resourcefullib.common.nbt.validators.numeric;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.nbt.validators.TagValidationType;
import com.teamresourceful.resourcefullib.common.nbt.validators.Validator;
import com.teamresourceful.resourcefullib.common.nbt.validators.ValidatorCodec;
import net.minecraft.nbt.NumericTag;

public interface NumericValidator extends Validator<NumericTag> {

    Codec<Validator<NumericTag>> CODEC = new ValidatorCodec<>(
            ExactNumericValidator.CODEC,
            (adder) -> {
                adder.add(ExactNumericValidator.ID, ExactNumericValidator.CODEC);
                adder.add(BetweenNumericValidator.ID, BetweenNumericValidator.CODEC);
            }
    );

    @Override
    default TagValidationType type() {
        return TagValidationType.NUMERIC;
    }
}
