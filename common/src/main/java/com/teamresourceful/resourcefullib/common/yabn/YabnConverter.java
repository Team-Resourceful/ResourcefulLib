package com.teamresourceful.resourcefullib.common.yabn;

import com.google.gson.*;
import com.teamresourceful.resourcefullib.common.yabn.base.YabnArray;
import com.teamresourceful.resourcefullib.common.yabn.base.YabnElement;
import com.teamresourceful.resourcefullib.common.yabn.base.YabnObject;
import com.teamresourceful.resourcefullib.common.yabn.base.YabnPrimitive;
import com.teamresourceful.resourcefullib.common.yabn.base.primitives.*;

import java.math.BigDecimal;

public final class YabnConverter {

    private YabnConverter() {
    }

    public static YabnElement fromJson(JsonElement json) {
        if (json instanceof JsonObject obj) {
            YabnObject object = new YabnObject();
            obj.entrySet().forEach(entry -> object.put(entry.getKey(), fromJson(entry.getValue())));
            return object;
        } else if (json instanceof JsonArray arr) {
            YabnArray array = new YabnArray();
            arr.forEach(element -> array.add(fromJson(element)));
            return array;
        } else if (json instanceof JsonPrimitive primitive) {
            if (primitive.isString()) return new YabnPrimitive(new StringContents(primitive.getAsString()));
            else if (primitive.isBoolean()) return new YabnPrimitive(new BooleanContents(primitive.getAsBoolean()));
            else if (primitive.isNumber()) {
                final BigDecimal value = primitive.getAsBigDecimal();
                try {
                    final long l = value.longValueExact();
                    return YabnCompressor.compressNonFloatingNumber(l);
                } catch (final ArithmeticException e) {
                    final double d = value.doubleValue();
                    return new YabnPrimitive((float) d == d ? new FloatContents((float) d) : new DoubleContents(d));
                }
            }
        } else if (json.isJsonNull()) {
            return new YabnPrimitive(NullContents.INSTANCE);
        }
        throw new IllegalArgumentException("Unsupported json element type: " + json.getClass().getName());
    }

    public static JsonElement toJson(YabnElement json) {
        if (json instanceof YabnObject obj) {
            JsonObject jsonObject = new JsonObject();
            obj.elements().forEach((key, value) -> jsonObject.add(key, toJson(value)));
            return jsonObject;
        } else if (json instanceof YabnArray arr) {
            JsonArray jsonArray = new JsonArray();
            arr.elements().forEach(element -> jsonArray.add(toJson(element)));
            return jsonArray;
        } else if (json instanceof YabnPrimitive primitive) {
            PrimitiveContents contents = primitive.contents();
            if (contents instanceof BooleanContents boolContents) return new JsonPrimitive(boolContents.value());
            if (contents instanceof ByteContents byteContents) return new JsonPrimitive(byteContents.value());
            if (contents instanceof DoubleContents doubleContents) return new JsonPrimitive(doubleContents.value());
            if (contents instanceof FloatContents floatContents) return new JsonPrimitive(floatContents.value());
            if (contents instanceof IntContents intContents) return new JsonPrimitive(intContents.value());
            if (contents instanceof LongContents longContents) return new JsonPrimitive(longContents.value());
            if (contents instanceof ShortContents shortContents) return new JsonPrimitive(shortContents.value());
            if (contents instanceof StringContents stringContents) return new JsonPrimitive(stringContents.value());
            if (contents instanceof NullContents) return JsonNull.INSTANCE;
            throw new IllegalArgumentException("Unknown primitive contents type: " + contents.getClass().getName());
        }
        throw new IllegalArgumentException("Unknown YABN element type: " + json.getClass().getName());
    }
}
