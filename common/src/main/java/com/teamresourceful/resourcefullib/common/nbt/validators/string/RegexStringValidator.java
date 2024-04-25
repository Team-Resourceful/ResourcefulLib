package com.teamresourceful.resourcefullib.common.nbt.validators.string;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.ExtraCodecs;

import java.util.regex.Pattern;

public record RegexStringValidator(Pattern value) implements StringValidator {

    public static final String ID = "string:regex";
    public static final Codec<RegexStringValidator> CODEC = ExtraCodecs.PATTERN
            .xmap(RegexStringValidator::new, RegexStringValidator::value)
            .fieldOf("string")
            .codec();

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean test(StringTag tag) {
        return value.matcher(tag.getAsString()).matches();
    }
}
