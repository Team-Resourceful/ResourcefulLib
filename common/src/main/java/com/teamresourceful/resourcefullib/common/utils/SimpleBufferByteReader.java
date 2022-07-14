package com.teamresourceful.resourcefullib.common.utils;

import com.teamresourceful.resourcefullib.common.utils.readers.ByteReader;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.ApiStatus;

/**
 * The reason for replcament is that it uses the base byte buf instead of Mojangs friendly byte buf.
 * @deprecated This has been replaced by {@link com.teamresourceful.resourcefullib.common.utils.readers.ByteBufByteReader}
 */
@Deprecated(since = "1.0.4", forRemoval = true)
@ApiStatus.ScheduledForRemoval(inVersion = "1.19.1")
public class SimpleBufferByteReader implements ByteReader {

    private final FriendlyByteBuf buf;

    public SimpleBufferByteReader(FriendlyByteBuf buf) {
        this.buf = buf;
    }

    @Override
    public byte peek() {
        return buf.getByte(buf.readerIndex());
    }

    @Override
    public void advance() {
        buf.readByte();
    }

    @Override
    public byte readByte() {
        return buf.readByte();
    }

}
