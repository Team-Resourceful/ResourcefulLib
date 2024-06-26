package com.teamresourceful.resourcefullib.common.utils;

import com.mojang.serialization.Codec;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.resourcefullib.common.codecs.EnumCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum TriState {
    TRUE,
    FALSE,
    UNDEFINED;

    public static final Codec<TriState> CODEC = EnumCodec.of(TriState.class);
    public static final ByteCodec<TriState> BYTE_CODEC = ByteCodec.ofEnum(TriState.class);
    public static final StreamCodec<ByteBuf, TriState> STREAM_CODEC = ByteBufCodecs.VAR_INT
            .map(i -> TriState.values()[i], TriState::ordinal);

    public static TriState of(boolean value) {
        return value ? TRUE : FALSE;
    }

    public static TriState of(Boolean value) {
        return value == null ? UNDEFINED : of(value.booleanValue());
    }

    public boolean isTrue() {
        return this == TRUE;
    }

    public boolean isFalse() {
        return this == FALSE;
    }

    public boolean isUndefined() {
        return !isDefined();
    }

    public boolean isDefined() {
        return this != UNDEFINED;
    }

    public boolean map(boolean value) {
        return this == UNDEFINED ? value : this == TRUE;
    }

    public static TriState map(TriState state, TriState value) {
        return state == null || state == UNDEFINED ? value : state;
    }

    public static TriState of(Number number) {
        if (number == null || number.longValue() >= 2) {
            return UNDEFINED;
        }
        return of(number.longValue() == 0);
    }
}