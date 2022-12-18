package com.teamresourceful.resourcefullib.common.nbt;

import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class ObjectTagReaders {

    public static <I> ObjectTagReader<I> create(boolean defaultReaders) {
        return new ObjectTagReader<>(defaultReaders);
    }

    public static <I> ObjectTagReader<I> create(boolean defaultReaders, String key, NbtReader<I, ? extends Tag> reader) {
        ObjectTagReader<I> objectTagReader = create(defaultReaders);
        objectTagReader.addReader(key, reader);
        return objectTagReader;
    }

    public static <I> ObjectTagReader<I> create(boolean defaultReaders, String key1, NbtReader<I, ? extends Tag> reader1, String key2, NbtReader<I, ? extends Tag> reader2) {
        ObjectTagReader<I> objectTagReader = create(defaultReaders, key1, reader1);
        objectTagReader.addReader(key2, reader2);
        return objectTagReader;
    }

    public static <I> ObjectTagReader<I> create(boolean defaultReaders, String key1, NbtReader<I, ? extends Tag> reader1, String key2, NbtReader<I, ? extends Tag> reader2, String key3, NbtReader<I, ? extends Tag> reader3) {
        ObjectTagReader<I> objectTagReader = create(defaultReaders, key1, reader1, key2, reader2);
        objectTagReader.addReader(key3, reader3);
        return objectTagReader;
    }

    public static <I> ObjectTagReader<I> create(boolean defaultReaders, String key1, NbtReader<I, ? extends Tag> reader1, String key2, NbtReader<I, ? extends Tag> reader2, String key3, NbtReader<I, ? extends Tag> reader3, String key4, NbtReader<I, ? extends Tag> reader4) {
        ObjectTagReader<I> objectTagReader = create(defaultReaders, key1, reader1, key2, reader2, key3, reader3);
        objectTagReader.addReader(key4, reader4);
        return objectTagReader;
    }

    public static <I> ObjectTagReader<I> create(boolean defaultReaders, String key1, NbtReader<I, ? extends Tag> reader1, String key2, NbtReader<I, ? extends Tag> reader2, String key3, NbtReader<I, ? extends Tag> reader3, String key4, NbtReader<I, ? extends Tag> reader4, String key5, NbtReader<I, ? extends Tag> reader5) {
        ObjectTagReader<I> objectTagReader = create(defaultReaders, key1, reader1, key2, reader2, key3, reader3, key4, reader4);
        objectTagReader.addReader(key5, reader5);
        return objectTagReader;
    }

    public static <I> ObjectTagReader<I> create(boolean defaultReaders, String key1, NbtReader<I, ? extends Tag> reader1, String key2, NbtReader<I, ? extends Tag> reader2, String key3, NbtReader<I, ? extends Tag> reader3, String key4, NbtReader<I, ? extends Tag> reader4, String key5, NbtReader<I, ? extends Tag> reader5, String key6, NbtReader<I, ? extends Tag> reader6) {
        ObjectTagReader<I> objectTagReader = create(defaultReaders, key1, reader1, key2, reader2, key3, reader3, key4, reader4, key5, reader5);
        objectTagReader.addReader(key6, reader6);
        return objectTagReader;
    }

    public static <I> ObjectTagReader<I> create(boolean defaultReaders, String key1, NbtReader<I, ? extends Tag> reader1, String key2, NbtReader<I, ? extends Tag> reader2, String key3, NbtReader<I, ? extends Tag> reader3, String key4, NbtReader<I, ? extends Tag> reader4, String key5, NbtReader<I, ? extends Tag> reader5, String key6, NbtReader<I, ? extends Tag> reader6, String key7, NbtReader<I, ? extends Tag> reader7) {
        ObjectTagReader<I> objectTagReader = create(defaultReaders, key1, reader1, key2, reader2, key3, reader3, key4, reader4, key5, reader5, key6, reader6);
        objectTagReader.addReader(key7, reader7);
        return objectTagReader;
    }

    public static <I> ObjectTagReader<I> create(boolean defaultReaders, String key1, NbtReader<I, ? extends Tag> reader1, String key2, NbtReader<I, ? extends Tag> reader2, String key3, NbtReader<I, ? extends Tag> reader3, String key4, NbtReader<I, ? extends Tag> reader4, String key5, NbtReader<I, ? extends Tag> reader5, String key6, NbtReader<I, ? extends Tag> reader6, String key7, NbtReader<I, ? extends Tag> reader7, String key8, NbtReader<I, ? extends Tag> reader8) {
        ObjectTagReader<I> objectTagReader = create(defaultReaders, key1, reader1, key2, reader2, key3, reader3, key4, reader4, key5, reader5, key6, reader6, key7, reader7);
        objectTagReader.addReader(key8, reader8);
        return objectTagReader;
    }

    public static <I> ObjectTagReader<I> create(boolean defaultReaders, String key1, NbtReader<I, ? extends Tag> reader1, String key2, NbtReader<I, ? extends Tag> reader2, String key3, NbtReader<I, ? extends Tag> reader3, String key4, NbtReader<I, ? extends Tag> reader4, String key5, NbtReader<I, ? extends Tag> reader5, String key6, NbtReader<I, ? extends Tag> reader6, String key7, NbtReader<I, ? extends Tag> reader7, String key8, NbtReader<I, ? extends Tag> reader8, String key9, NbtReader<I, ? extends Tag> reader9) {
        ObjectTagReader<I> objectTagReader = create(defaultReaders, key1, reader1, key2, reader2, key3, reader3, key4, reader4, key5, reader5, key6, reader6, key7, reader7, key8, reader8);
        objectTagReader.addReader(key9, reader9);
        return objectTagReader;
    }

    public static <I> ObjectTagReader<I> create(boolean defaultReaders, String key1, NbtReader<I, ? extends Tag> reader1, String key2, NbtReader<I, ? extends Tag> reader2, String key3, NbtReader<I, ? extends Tag> reader3, String key4, NbtReader<I, ? extends Tag> reader4, String key5, NbtReader<I, ? extends Tag> reader5, String key6, NbtReader<I, ? extends Tag> reader6, String key7, NbtReader<I, ? extends Tag> reader7, String key8, NbtReader<I, ? extends Tag> reader8, String key9, NbtReader<I, ? extends Tag> reader9, String key10, NbtReader<I, ? extends Tag> reader10) {
        ObjectTagReader<I> objectTagReader = create(defaultReaders, key1, reader1, key2, reader2, key3, reader3, key4, reader4, key5, reader5, key6, reader6, key7, reader7, key8, reader8, key9, reader9);
        objectTagReader.addReader(key10, reader10);
        return objectTagReader;
    }
}
