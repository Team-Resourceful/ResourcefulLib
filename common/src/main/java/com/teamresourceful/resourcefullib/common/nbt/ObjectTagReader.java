package com.teamresourceful.resourcefullib.common.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

@ApiStatus.Experimental
public class ObjectTagReader<I> implements NbtReader<I, CompoundTag> {

    private final boolean defaultReaders;
    private final Map<String, NbtReader<I, ? extends Tag>> readers = new HashMap<>();

    public ObjectTagReader(boolean defaultReaders) {
        this.defaultReaders = defaultReaders;
    }

    public void addReader(String key, NbtReader<I, ? extends Tag> reader) {
        readers.put(key, reader);
    }

    @Override
    public byte type() {
        return Tag.TAG_COMPOUND;
    }

    @Override
    public void setDefault(I instance) {
       if (defaultReaders) {
            readers.forEach((key, reader) -> reader.setDefault(instance));
       }
    }

    @Override
    public void read(I instance, CompoundTag tag) {
        readers.forEach((key, reader) -> {
            if (tag.contains(key, reader.type())) {
                reader.castRead(instance, tag.get(key));
            } else {
                reader.setDefault(instance);
            }
        });
    }

    @Override
    public CompoundTag write(I instance) {
        CompoundTag tag = new CompoundTag();
        write(instance, tag);
        return tag;
    }

    public void write(I instance, CompoundTag tag) {
        readers.forEach((key, reader) -> tag.put(key, reader.write(instance)));
    }
}
