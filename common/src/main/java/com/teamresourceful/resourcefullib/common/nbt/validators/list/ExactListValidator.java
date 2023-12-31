package com.teamresourceful.resourcefullib.common.nbt.validators.list;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.teamresourceful.resourcefullib.common.nbt.validators.TagValidationType;
import com.teamresourceful.resourcefullib.common.nbt.validators.Validator;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record ExactListValidator(List<Validator<?>> validators) implements ListValidator {

    public static final String ID = "list:exact";

    public static final Codec<ExactListValidator> CODEC = ExtraCodecs.validate(
            Validator.CODEC.listOf(),
            validators -> {
                TagValidationType type = null;
                for (Validator<?> validator : validators) {
                    if (type == null) {
                        type = validator.type();
                    } else if (type != validator.type()) {
                        return DataResult.error(() -> "All validators must be of the same type.");
                    }
                }
                return DataResult.success(validators);
            }
    ).xmap(ExactListValidator::new, ExactListValidator::validators);

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean test(CollectionTag<?> tag) {
        if (tag.isEmpty()) return false;
        if (tag.size() != validators.size()) return false;
        for (int i = 0; i < tag.size(); i++) {
            if (!validators.get(i).testAndValidate(tag.get(i))) {
                return false;
            }
        }
        return true;
    }
}
