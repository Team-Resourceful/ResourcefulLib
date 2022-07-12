package com.teamresourceful.resourcefullib.common.yabn;

import com.teamresourceful.resourcefullib.common.yabn.base.*;
import com.teamresourceful.resourcefullib.common.yabn.base.primitives.*;
import com.teamresourceful.resourcefullib.common.utils.SimpleByteReader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class YabnParser {

    private YabnParser() {
    }

    public static YabnElement parseCompress(SimpleByteReader data) throws YabnException {
        return YabnCompressor.compress(parse(data));
    }

    public static YabnElement parse(SimpleByteReader data) throws YabnException {
        try {
            YabnType type = YabnType.fromId(data.readByte());
            return getElement(type, data);
        }catch (Exception exception) {
            if (exception instanceof YabnException) {
                throw exception;
            } else if (exception instanceof ArrayIndexOutOfBoundsException) {
                throw new YabnException("Array index out of bounds, make sure there is an EOD byte at the end of the data that requires it.");
            } else {
                throw new YabnException(exception.getMessage());
            }
        }
    }

    private static YabnElement read(SimpleByteReader data) {
        Map<String, YabnElement> obj = new LinkedHashMap<>();
        while (data.peek() != YabnElement.EOD) {
            YabnType type = YabnType.fromId(data.readByte());
            String key = data.readString();
            obj.put(key, getElement(type, data));
        }
        data.readByte(); // skip 0x00 because it needs to go to the next elements.
        return new YabnObject(obj);
    }

    private static YabnArray readArray(SimpleByteReader data) {
        List<YabnElement> elements = new ArrayList<>();
        while (data.peek() != YabnElement.EOD) {
            YabnType type = YabnType.fromId(data.readByte());
            elements.add(getElement(type, data));
        }
        data.readByte(); // skip 0x00 because it needs to go to the next elements.
        return new YabnArray(elements);
    }

    private static YabnElement getElement(YabnType type, SimpleByteReader data) {
        return switch (type) {
            case NULL -> NullContents.NULL;
            case BOOLEAN_TRUE -> YabnPrimitive.ofBoolean(true);
            case BOOLEAN_FALSE -> YabnPrimitive.ofBoolean(false);
            case BYTE -> YabnPrimitive.ofByte(data.readByte());
            case SHORT -> YabnPrimitive.ofShort(data.readShort());
            case INT -> YabnPrimitive.ofInt(data.readInt());
            case LONG -> YabnPrimitive.ofLong(data.readLong());
            case FLOAT -> YabnPrimitive.ofFloat(data.readFloat());
            case DOUBLE -> YabnPrimitive.ofDouble(data.readDouble());
            case STRING -> YabnPrimitive.ofString(data.readString());
            case EMPTY_STRING -> YabnPrimitive.ofString("");
            case ARRAY -> readArray(data);
            case OBJECT -> read(data);
            case EMPTY_ARRAY -> new YabnArray();
            case EMPTY_OBJECT -> new YabnObject();
        };
    }
}
