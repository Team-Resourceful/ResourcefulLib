package com.teamresourceful.resourcefullib.common.nbt.validators.numeric;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import net.minecraft.nbt.*;

import java.util.Optional;

public record BetweenNumericValidator(Optional<Number> min, Optional<Number> max) implements NumericValidator {

    public static final String ID = "number:between";
    public static final Codec<BetweenNumericValidator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecExtras.NUMBER.optionalFieldOf("min").forGetter(BetweenNumericValidator::min),
            CodecExtras.NUMBER.optionalFieldOf("max").forGetter(BetweenNumericValidator::max)
    ).apply(instance, BetweenNumericValidator::new));

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean test(NumericTag tag) {
        return switch (tag) {
            case ByteTag byteTag -> {
                byte min = this.min.map(Number::byteValue).orElse(Byte.MIN_VALUE);
                byte max = this.max.map(Number::byteValue).orElse(Byte.MAX_VALUE);
                yield byteTag.value() >= min && byteTag.value() <= max;
            }
            case ShortTag shortTag -> {
                short min = this.min.map(Number::shortValue).orElse(Short.MIN_VALUE);
                short max = this.max.map(Number::shortValue).orElse(Short.MAX_VALUE);
                yield shortTag.value() >= min && shortTag.value() <= max;
            }
            case IntTag intTag -> {
                int min = this.min.map(Number::intValue).orElse(Integer.MIN_VALUE);
                int max = this.max.map(Number::intValue).orElse(Integer.MAX_VALUE);
                yield intTag.value() >= min && intTag.value() <= max;
            }
            case LongTag longTag -> {
                long min = this.min.map(Number::longValue).orElse(Long.MIN_VALUE);
                long max = this.max.map(Number::longValue).orElse(Long.MAX_VALUE);
                yield longTag.value() >= min && longTag.value() <= max;
            }
            case FloatTag floatTag -> {
                float min = this.min.map(Number::floatValue).orElse(Float.MIN_VALUE);
                float max = this.max.map(Number::floatValue).orElse(Float.MAX_VALUE);
                yield floatTag.value() >= min && floatTag.value() <= max;
            }
            case DoubleTag doubleTag -> {
                double min = this.min.map(Number::doubleValue).orElse(Double.MIN_VALUE);
                double max = this.max.map(Number::doubleValue).orElse(Double.MAX_VALUE);
                yield doubleTag.value() >= min && doubleTag.value() <= max;
            }
            // Should never happen unless someone makes a custom tag that extends NumericTag
            case null, default -> false;
        };
    }
}
