package com.teamresourceful.resourcefullib.common.nbt.validators.numeric;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import net.minecraft.nbt.*;

public record ExactNumericValidator(Number number) implements NumericValidator {

    public static final String ID = "number:exact";
    public static final Codec<ExactNumericValidator> CODEC = CodecExtras.NUMBER
            .xmap(ExactNumericValidator::new, ExactNumericValidator::number);

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean test(NumericTag tag) {
        if (tag instanceof ByteTag byteTag) {
            return byteTag.getAsByte() == number.byteValue();
        } else if (tag instanceof ShortTag shortTag) {
            return shortTag.getAsShort() == number.shortValue();
        } else if (tag instanceof IntTag intTag) {
            return intTag.getAsInt() == number.intValue();
        } else if (tag instanceof LongTag longTag) {
            return longTag.getAsLong() == number.longValue();
        } else if (tag instanceof FloatTag floatTag) {
            return floatTag.getAsFloat() == number.floatValue();
        } else if (tag instanceof DoubleTag doubleTag) {
            return doubleTag.getAsDouble() == number.doubleValue();
        } else {
            // Should never happen unless someone makes a custom tag that extends NumericTag
            return tag.getAsNumber().equals(number);
        }
    }
}
