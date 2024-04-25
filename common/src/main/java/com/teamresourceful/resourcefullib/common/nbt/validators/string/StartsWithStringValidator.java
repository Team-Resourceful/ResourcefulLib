package com.teamresourceful.resourcefullib.common.nbt.validators.string;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.StringTag;

public record StartsWithStringValidator(String value) implements StringValidator {

    public static final String ID = "string:starts_with";
    public static final Codec<StartsWithStringValidator> CODEC = Codec.STRING
            .xmap(StartsWithStringValidator::new, StartsWithStringValidator::value)
            .fieldOf("string")
            .codec();

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean test(StringTag tag) {
        return tag.getAsString().startsWith(value);
    }
}
