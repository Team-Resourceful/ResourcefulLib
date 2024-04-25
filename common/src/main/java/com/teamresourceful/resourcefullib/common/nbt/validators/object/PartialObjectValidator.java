package com.teamresourceful.resourcefullib.common.nbt.validators.object;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.nbt.validators.Validator;
import net.minecraft.nbt.CompoundTag;

import java.util.Map;

public record PartialObjectValidator(Map<String, Validator<?>> validators) implements ObjectValidator {

    public static final String ID = "object:partial";
    public static final Codec<PartialObjectValidator> CODEC = Codec.unboundedMap(Codec.STRING, Validator.CODEC)
            .xmap(PartialObjectValidator::new, PartialObjectValidator::validators)
            .fieldOf("object")
            .codec();

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean test(CompoundTag tag) {
        for (var entry : validators.entrySet()) {
            if (!tag.contains(entry.getKey())) {
                return false;
            }
            if (!entry.getValue().testAndValidate(tag.get(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }
}
