package com.teamresourceful.resourcefullib.common.nbt.validators.string;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.StringTag;

public record EndsWithStringValidator(String value) implements StringValidator {

    public static final String ID = "string:ends_with";
    public static final Codec<EndsWithStringValidator> CODEC = Codec.STRING
            .xmap(EndsWithStringValidator::new, EndsWithStringValidator::value);

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean test(StringTag tag) {
        return tag.getAsString().endsWith(value);
    }
}
