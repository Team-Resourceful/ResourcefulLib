package com.teamresourceful.resourcefullib.common.yabn.base.primitives;

public interface NumberPrimitiveContents extends PrimitiveContents {

    Number getValue();

    default byte getAsByte() {
        return getValue().byteValue();
    }

    default short getShortValue() {
        return getValue().shortValue();
    }

    default int getAsInt() {
        return getValue().intValue();
    }

    default long getAsLong() {
        return getValue().longValue();
    }

    default float getAsFloat() {
        return getValue().floatValue();
    }

    default double getAsDouble() {
        return getValue().doubleValue();
    }

}
