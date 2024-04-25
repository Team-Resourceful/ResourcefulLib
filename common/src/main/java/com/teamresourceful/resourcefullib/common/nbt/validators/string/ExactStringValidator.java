package com.teamresourceful.resourcefullib.common.nbt.validators.string;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.StringTag;

public record ExactStringValidator(String value) implements StringValidator {

    public static final String ID = "string:exact";
    public static final Codec<ExactStringValidator> CODEC = Codec.STRING
            .xmap(ExactStringValidator::new, ExactStringValidator::value)
            .fieldOf("string")
            .codec();

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean test(StringTag tag) {
        return tag.getAsString().equals(value);
    }
}
