package com.teamresourceful.resourcefullib.common.nbt.validators.object;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.nbt.validators.Validator;
import net.minecraft.nbt.CompoundTag;

import java.util.Map;

public record ExactObjectValidator(Map<String, Validator<?>> validators) implements ObjectValidator {

    public static final String ID = "object:exact";
    public static final Codec<ExactObjectValidator> CODEC = Codec.unboundedMap(Codec.STRING, Validator.CODEC)
            .xmap(ExactObjectValidator::new, ExactObjectValidator::validators);

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean test(CompoundTag tag) {
        for (String key : tag.getAllKeys()) {
            if (!validators.containsKey(key) || !validators.get(key).testAndValidate(tag.get(key))) {
                return false;
            }
        }
        for (String key : validators.keySet()) {
            if (!tag.contains(key)) {
                return false;
            }
        }
        return true;
    }

}
