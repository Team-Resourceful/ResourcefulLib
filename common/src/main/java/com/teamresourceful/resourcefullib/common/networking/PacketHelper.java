package com.teamresourceful.resourcefullib.common.networking;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.teamresourceful.resourcefullib.common.codecs.yabn.YabnOps;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import com.teamresourceful.yabn.YabnParser;
import com.teamresourceful.yabn.elements.YabnElement;
import com.teamresourceful.yabn.reader.ByteReader;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.RegistryOps;

import java.util.Optional;

public final class PacketHelper {

    private PacketHelper() throws UtilityClassException {
        throw new UtilityClassException();
    }

    public static <T> DataResult<YabnElement> writeWithYabn(FriendlyByteBuf buf, Codec<T> codec, T object, boolean compressed) {
        return writeWithYabnInternal(compressed ? YabnOps.COMPRESSED : YabnOps.INSTANCE, buf, codec, object);
    }

    public static <T> DataResult<YabnElement> writeWithRegistryYabn(RegistryAccess access, FriendlyByteBuf buf, Codec<T> codec, T object, boolean compressed) {
        return writeWithYabnInternal(RegistryOps.create(compressed ? YabnOps.COMPRESSED : YabnOps.INSTANCE, access), buf, codec, object);
    }

    private static <T> DataResult<YabnElement> writeWithYabnInternal(DynamicOps<YabnElement> ops, FriendlyByteBuf buf, Codec<T> codec, T object) {
        DataResult<YabnElement> result = codec.encodeStart(ops, object);
        Optional<YabnElement> optional = result.result();
        if (optional.isPresent()) {
            buf.writeBytes(optional.get().toFullData());
            return result;
        }
        return result;
    }

    public static <T> DataResult<T> readWithYabn(FriendlyByteBuf buf, Codec<T> codec, boolean compressed) {
        return readWithYabnInternal(compressed ? YabnOps.COMPRESSED : YabnOps.INSTANCE, buf, codec);
    }

    public static <T> DataResult<T> readWithRegistryYabn(RegistryAccess access, FriendlyByteBuf buf, Codec<T> codec, boolean compressed) {
        return readWithYabnInternal(RegistryOps.create(compressed ? YabnOps.COMPRESSED : YabnOps.INSTANCE, access), buf, codec);
    }

    private static <T> DataResult<T> readWithYabnInternal(DynamicOps<YabnElement> ops, FriendlyByteBuf buf, Codec<T> codec) {
        try {
            return codec.parse(ops, YabnParser.parse(new ByteBufByteReader(buf)));
        } catch (Exception e) {
            return DataResult.error(e::getMessage);
        }
    }

    private record ByteBufByteReader(ByteBuf buf) implements ByteReader {

        @Override
        public byte peek() {
            return buf.getByte(buf.readerIndex());
        }

        @Override
        public void advance() {
            buf.skipBytes(1);
        }

        @Override
        public byte readByte() {
            return buf.readByte();
        }
    }
}