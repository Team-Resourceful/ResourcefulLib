package com.teamresourceful.resourcefullib.common.yabn.base;

/**
 * This contains the byte values person YABN type and special cases such as bool false and true get 2 separate bytes for smaller data size.
 * Note: This special case can be done for more types but boolean is the only one that is special right now.
 */
public enum YabnType {
    NULL(0x01),
    BOOLEAN_TRUE(0x02),
    BOOLEAN_FALSE(0x03),
    BYTE(0x04),
    SHORT(0x05),
    INT(0x06),
    LONG(0x07),
    DOUBLE(0x08),
    FLOAT(0x09),
    STRING(0x0A),
    ARRAY(0x0B),
    OBJECT(0x0C);

    public final byte id;

    YabnType(int id) {
        this.id = (byte) id;
    }

    public static YabnType fromId(byte id) {
        for (YabnType value : YabnType.values()) {
            if (value.id == id) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid YABN type id: " + id);
    }
}
