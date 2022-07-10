package com.teamresourceful.resourcefullib.common.yabn;

import com.teamresourceful.resourcefullib.common.yabn.base.*;
import com.teamresourceful.resourcefullib.common.yabn.base.primitives.*;
import com.teamresourceful.resourcefullib.common.utils.SimpleByteReader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class YabnParser {

    public static YabnElement parse(SimpleByteReader data) {
        YabnType type = YabnType.fromId(data.readByte());
        return getElement(type, data);
    }

    private static YabnElement read(SimpleByteReader data) {
        Map<String, YabnElement> obj = new LinkedHashMap<>();
        do {
            YabnType type = YabnType.fromId(data.readByte());
            String key = data.readString();
            obj.put(key, getElement(type, data));
        } while (data.getByte() != YabnElement.EOD);
        data.readByte(); // skip 0x00 because it needs to go to the next elements.
        return new YabnObject(obj);
    }

    private static YabnArray readArray(SimpleByteReader data) {
        List<YabnElement> elements = new ArrayList<>();
        do {
            YabnType type = YabnType.fromId(data.readByte());
            elements.add(getElement(type, data));
        } while (data.getByte() != YabnElement.EOD);
        data.readByte(); // skip 0x00 because it needs to go to the next elements.
        return new YabnArray(elements);
    }

    private static YabnElement getElement(YabnType type, SimpleByteReader data) {
        return switch (type) {
            case NULL -> new YabnPrimitive(NullContents.INSTANCE);
            case BOOLEAN_TRUE -> new YabnPrimitive(new BooleanContents(true));
            case BOOLEAN_FALSE -> new YabnPrimitive(new BooleanContents(false));
            case BYTE -> new YabnPrimitive(new ByteContents(data.readByte()));
            case SHORT -> new YabnPrimitive(new ShortContents(data.readShort()));
            case INT -> new YabnPrimitive(new IntContents(data.readInt()));
            case LONG -> new YabnPrimitive(new LongContents(data.readLong()));
            case FLOAT -> new YabnPrimitive(new FloatContents(data.readFloat()));
            case DOUBLE -> new YabnPrimitive(new DoubleContents(data.readDouble()));
            case STRING -> new YabnPrimitive(new StringContents(data.readString()));
            case ARRAY -> readArray(data);
            case OBJECT -> read(data);
        };
    }
}
