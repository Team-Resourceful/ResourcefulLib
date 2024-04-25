package com.teamresourceful.resourcefullib.common.nbt.validators.list;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.nbt.validators.Validator;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.Tag;

public record MatchesListValidator(Validator<?> validator) implements ListValidator {

    public static final String ID = "list:matches";
    public static final Codec<MatchesListValidator> CODEC = Validator.CODEC
            .xmap(MatchesListValidator::new, MatchesListValidator::validator)
            .fieldOf("validators")
            .codec();

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean test(CollectionTag<?> tag) {
        if (tag.isEmpty()) return false;
        for (Tag value : tag) {
            if (!validator.testAndValidate(value)) {
                return false;
            }
        }
        return true;
    }
}
