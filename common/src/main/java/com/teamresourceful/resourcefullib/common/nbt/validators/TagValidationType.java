package com.teamresourceful.resourcefullib.common.nbt.validators;

import it.unimi.dsi.fastutil.bytes.BytePredicate;
import net.minecraft.nbt.*;

import java.util.function.Predicate;

public record TagValidationType(BytePredicate type) implements Predicate<Tag> {

    public static final TagValidationType STRING = new TagValidationType(type -> Tag.TAG_STRING == type);
    public static final TagValidationType LIST = new TagValidationType(type ->
            Tag.TAG_LIST == type || Tag.TAG_BYTE_ARRAY == type ||
            Tag.TAG_INT_ARRAY == type || Tag.TAG_LONG_ARRAY == type
    );
    public static final TagValidationType COMPOUND = new TagValidationType(type -> Tag.TAG_COMPOUND == type);
    public static final TagValidationType NUMERIC = new TagValidationType(type ->
            Tag.TAG_BYTE == type || Tag.TAG_SHORT == type ||
            Tag.TAG_INT == type || Tag.TAG_LONG == type ||
            Tag.TAG_FLOAT == type || Tag.TAG_DOUBLE == type
    );

    @Override
    public boolean test(Tag t) {
        return type.test(t.getId());
    }
}
