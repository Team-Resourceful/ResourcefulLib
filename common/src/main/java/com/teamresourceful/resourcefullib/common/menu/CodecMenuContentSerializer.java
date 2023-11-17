package com.teamresourceful.resourcefullib.common.menu;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public interface CodecMenuContentSerializer<T extends MenuContent<T>> extends MenuContentSerializer<T> {

    ByteCodec<T> codec();

    @Override
    default @Nullable T from(FriendlyByteBuf buffer) {
        return codec().decode(buffer);
    }

    @Override
    default void to(FriendlyByteBuf buffer, T content) {
        codec().encode(content, buffer);
    }
}
