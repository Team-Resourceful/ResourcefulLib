package com.teamresourceful.resourcefullib.common.nbt;

import net.minecraft.nbt.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@ApiStatus.Experimental
public class TagReader<I, T extends Tag> implements NbtReader<I, T> {

    private final byte type;
    private final Function<I, T> writer;
    private final BiConsumer<I, T> reader;
    @Nullable
    private final Supplier<T> defaultCreator;

    public TagReader(byte type, Function<I, T> writer, BiConsumer<I, T> reader, @Nullable Supplier<T> defaultCreator) {
        this.type = type;
        this.writer = writer;
        this.reader = reader;
        this.defaultCreator = defaultCreator;
    }

    public static <I> TagReader<I, ByteTag> byteTag(Function<I, Byte> writer, BiConsumer<I, Byte> reader) {
        return new TagReader<>(Tag.TAG_BYTE, i -> ByteTag.valueOf(writer.apply(i)), (i, t) -> reader.accept(i, t.getAsByte()), null);
    }

    public static <I> TagReader<I, ByteTag> byteTag(Function<I, Byte> writer, BiConsumer<I, Byte> reader, byte defaultValue) {
        return new TagReader<>(Tag.TAG_BYTE, i -> ByteTag.valueOf(writer.apply(i)), (i, t) -> reader.accept(i, t.getAsByte()), () -> ByteTag.valueOf(defaultValue));
    }

    public static <I> TagReader<I, ShortTag> shortTag(Function<I, Short> writer, BiConsumer<I, Short> reader) {
        return new TagReader<>(Tag.TAG_SHORT, i -> ShortTag.valueOf(writer.apply(i)), (i, t) -> reader.accept(i, t.getAsShort()), null);
    }

    public static <I> TagReader<I, ShortTag> shortTag(Function<I, Short> writer, BiConsumer<I, Short> reader, short defaultValue) {
        return new TagReader<>(Tag.TAG_SHORT, i -> ShortTag.valueOf(writer.apply(i)), (i, t) -> reader.accept(i, t.getAsShort()), () -> ShortTag.valueOf(defaultValue));
    }

    public static <I> TagReader<I, IntTag> intTag(Function<I, Integer> writer, BiConsumer<I, Integer> reader) {
        return new TagReader<>(Tag.TAG_INT, i -> IntTag.valueOf(writer.apply(i)), (i, t) -> reader.accept(i, t.getAsInt()), null);
    }

    public static <I> TagReader<I, IntTag> intTag(Function<I, Integer> writer, BiConsumer<I, Integer> reader, int defaultValue) {
        return new TagReader<>(Tag.TAG_INT, i -> IntTag.valueOf(writer.apply(i)), (i, t) -> reader.accept(i, t.getAsInt()), () -> IntTag.valueOf(defaultValue));
    }

    public static <I> TagReader<I, LongTag> longTag(Function<I, Long> writer, BiConsumer<I, Long> reader) {
        return new TagReader<>(Tag.TAG_LONG, i -> LongTag.valueOf(writer.apply(i)), (i, t) -> reader.accept(i, t.getAsLong()), null);
    }

    public static <I> TagReader<I, LongTag> longTag(Function<I, Long> writer, BiConsumer<I, Long> reader, long defaultValue) {
        return new TagReader<>(Tag.TAG_LONG, i -> LongTag.valueOf(writer.apply(i)), (i, t) -> reader.accept(i, t.getAsLong()), () -> LongTag.valueOf(defaultValue));
    }

    public static <I> TagReader<I, FloatTag> floatTag(Function<I, Float> writer, BiConsumer<I, Float> reader) {
        return new TagReader<>(Tag.TAG_FLOAT, i -> FloatTag.valueOf(writer.apply(i)), (i, t) -> reader.accept(i, t.getAsFloat()), null);
    }

    public static <I> TagReader<I, FloatTag> floatTag(Function<I, Float> writer, BiConsumer<I, Float> reader, float defaultValue) {
        return new TagReader<>(Tag.TAG_FLOAT, i -> FloatTag.valueOf(writer.apply(i)), (i, t) -> reader.accept(i, t.getAsFloat()), () -> FloatTag.valueOf(defaultValue));
    }

    public static <I> TagReader<I, DoubleTag> doubleTag(Function<I, Double> writer, BiConsumer<I, Double> reader) {
        return new TagReader<>(Tag.TAG_DOUBLE, i -> DoubleTag.valueOf(writer.apply(i)), (i, t) -> reader.accept(i, t.getAsDouble()), null);
    }

    public static <I> TagReader<I, DoubleTag> doubleTag(Function<I, Double> writer, BiConsumer<I, Double> reader, double defaultValue) {
        return new TagReader<>(Tag.TAG_DOUBLE, i -> DoubleTag.valueOf(writer.apply(i)), (i, t) -> reader.accept(i, t.getAsDouble()), () -> DoubleTag.valueOf(defaultValue));
    }

    public static <I> TagReader<I, StringTag> stringTag(Function<I, String> writer, BiConsumer<I, String> reader) {
        return new TagReader<>(Tag.TAG_STRING, i -> StringTag.valueOf(writer.apply(i)), (i, t) -> reader.accept(i, t.getAsString()), null);
    }

    public static <I> TagReader<I, StringTag> stringTag(Function<I, String> writer, BiConsumer<I, String> reader, String defaultValue) {
        return new TagReader<>(Tag.TAG_STRING, i -> StringTag.valueOf(writer.apply(i)), (i, t) -> reader.accept(i, t.getAsString()), () -> StringTag.valueOf(defaultValue));
    }

    @Override
    public byte type() {
        return this.type;
    }

    @Override
    public void setDefault(I instance) {
        if (this.defaultCreator != null) {
            this.reader.accept(instance, this.defaultCreator.get());
        }
    }

    @Override
    public void read(I instance, T tag) {
        this.reader.accept(instance, tag);
    }

    @Override
    public T write(I instance) {
        return this.writer.apply(instance);
    }
}
