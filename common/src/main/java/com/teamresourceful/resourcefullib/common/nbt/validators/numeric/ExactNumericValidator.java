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
            case ByteTag byteTag -> byteTag.value() == number.byteValue();
            case ShortTag shortTag -> shortTag.value() == number.shortValue();
            case IntTag intTag -> intTag.value() == number.intValue();
            case LongTag longTag -> longTag.value() == number.longValue();
            case FloatTag floatTag -> floatTag.value() == number.floatValue();
            case DoubleTag doubleTag -> doubleTag.value() == number.doubleValue();
            case null -> false;

            // Should never happen unless someone makes a custom tag that extends NumericTag
            default -> tag.box().equals(number);
        };
    }
}
