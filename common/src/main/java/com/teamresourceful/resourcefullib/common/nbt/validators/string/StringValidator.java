package com.teamresourceful.resourcefullib.common.nbt.validators.string;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.nbt.validators.TagValidationType;
import com.teamresourceful.resourcefullib.common.nbt.validators.Validator;
import com.teamresourceful.resourcefullib.common.nbt.validators.ValidatorCodec;
import net.minecraft.nbt.StringTag;

public interface StringValidator extends Validator<StringTag> {

    Codec<Validator<StringTag>> CODEC = new ValidatorCodec<>(
            ExactStringValidator.CODEC,
            (adder) -> {
                adder.add(ExactStringValidator.ID, ExactStringValidator.CODEC);
                adder.add(ContainsStringValidator.ID, ContainsStringValidator.CODEC);
                adder.add(StartsWithStringValidator.ID, StartsWithStringValidator.CODEC);
                adder.add(EndsWithStringValidator.ID, EndsWithStringValidator.CODEC);
                adder.add(RegexStringValidator.ID, RegexStringValidator.CODEC);
            }
    );

    @Override
    default TagValidationType type() {
        return TagValidationType.STRING;
    }
}
