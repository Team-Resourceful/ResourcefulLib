package com.teamresourceful.resourcefullib.common.nbt.validators.numeric;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import net.minecraft.nbt.*;

public record ExactNumericValidator(Number number) implements NumericValidator {

    public static final String ID = "number:exact";
    public static final Codec<ExactNumericValidator> CODEC = CodecExtras.NUMBER
            .xmap(ExactNumericValidator::new, ExactNumericValidator::number)
            .fieldOf("number")
            .codec();

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean test(NumericTag tag) {
        return switch (tag) {
            case ByteTag byteTag -> byteTag.getAsByte() == number.byteValue();
            case ShortTag shortTag -> shortTag.getAsShort() == number.shortValue();
            case IntTag intTag -> intTag.getAsInt() == number.intValue();
            case LongTag longTag -> longTag.getAsLong() == number.longValue();
            case FloatTag floatTag -> floatTag.getAsFloat() == number.floatValue();
            case DoubleTag doubleTag -> doubleTag.getAsDouble() == number.doubleValue();
            case null -> false;

            // Should never happen unless someone makes a custom tag that extends NumericTag
            default -> tag.getAsNumber().equals(number);
        };
    }
}
