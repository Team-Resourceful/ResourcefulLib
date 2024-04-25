package com.teamresourceful.resourcefullib.common.nbt.validators.string;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.StringTag;

public record ContainsStringValidator(String value) implements StringValidator {

    public static final String ID = "string:contains";
    public static final Codec<ContainsStringValidator> CODEC = Codec.STRING
            .xmap(ContainsStringValidator::new, ContainsStringValidator::value)
            .fieldOf("string")
            .codec();

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean test(StringTag tag) {
        return tag.getAsString().contains(value);
    }
}
