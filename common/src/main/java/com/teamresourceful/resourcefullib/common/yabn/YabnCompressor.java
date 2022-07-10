package com.teamresourceful.resourcefullib.common.yabn;

import com.teamresourceful.resourcefullib.common.yabn.base.YabnArray;
import com.teamresourceful.resourcefullib.common.yabn.base.YabnElement;
import com.teamresourceful.resourcefullib.common.yabn.base.YabnObject;
import com.teamresourceful.resourcefullib.common.yabn.base.YabnPrimitive;
import com.teamresourceful.resourcefullib.common.yabn.base.primitives.*;

public final class YabnCompressor {

    private YabnCompressor() {
    }

    public static YabnElement compress(YabnElement element) {
        if (element instanceof YabnObject object) {
            YabnObject newObject = new YabnObject();
            for (var entry : object.elements().entrySet()) {
                newObject.put(entry.getKey(), compress(entry.getValue()));
            }
            return object;
        } else if (element instanceof YabnArray array) {
            YabnArray newArray = new YabnArray();
            for (var item : array.elements()) {
                newArray.add(compress(item));
            }
            return newArray;
        } else if (element instanceof YabnPrimitive primitive) {
            if (primitive.contents() instanceof NumberPrimitiveContents numberContent) {
                if (numberContent instanceof DoubleContents doubles) {
                    if ((float)doubles.getAsDouble() == doubles.getAsDouble()) {
                        return new YabnPrimitive(new FloatContents(doubles.getAsFloat()));
                    }
                } else if (!(numberContent instanceof FloatContents)) {
                    return compressNonFloatingNumber(numberContent.getAsLong());
                }
            }
        }
        return element;
    }

    public static YabnElement compressNonFloatingNumber(long l) {
        if ((byte) l == l) return new YabnPrimitive(new ByteContents((byte) l));
        else if ((short) l == l) return new YabnPrimitive(new ShortContents((short) l));
        else if ((int) l == l) return new YabnPrimitive(new IntContents((int) l));
        return new YabnPrimitive(new LongContents(l));
    }

}
