package com.teamresourceful.resourcefullib.common.networking;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.teamresourceful.resourcefullib.common.codecs.yabn.YabnOps;
import com.teamresourceful.resourcefullib.common.utils.readers.ByteBufByteReader;
import com.teamresourceful.resourcefullib.common.yabn.YabnParser;
import com.teamresourceful.resourcefullib.common.yabn.base.YabnElement;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Optional;

public final class PacketHelper {

    private PacketHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> DataResult<YabnElement> writeWithYabn(FriendlyByteBuf buf, Codec<T> codec, T object, boolean compressed) {
        YabnOps ops = compressed ? YabnOps.COMPRESSED : YabnOps.INSTANCE;
        DataResult<YabnElement> result = codec.encodeStart(ops, object);
        Optional<YabnElement> optional = result.result();
        if (optional.isPresent()) {
            buf.writeBytes(optional.get().toData());
            return result;
        }
        return result;
    }

    public static <T> DataResult<T> readWithYabn(FriendlyByteBuf buf, Codec<T> codec, boolean compressed) {
        try {
            YabnOps ops = compressed ? YabnOps.COMPRESSED : YabnOps.INSTANCE;
            return codec.parse(ops, YabnParser.parse(new ByteBufByteReader(buf)));
        } catch (Exception e) {
            return DataResult.error(e.getMessage());
        }
    }
}