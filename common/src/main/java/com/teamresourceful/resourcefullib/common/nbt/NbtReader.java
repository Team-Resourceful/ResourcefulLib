package com.teamresourceful.resourcefullib.common.nbt;

import net.minecraft.nbt.Tag;

public interface NbtReader<I, T extends Tag> {

    /**
     * Gets the type of tag this reader reads
     * @see net.minecraft.nbt.Tag#TAG_COMPOUND
     */
    byte type();

    /**
     * Sets the default value for the instance for this reader.
     * @param instance the instance to set the default value for.
     */
    void setDefault(I instance);

    /**
     * This is used to cast the tag to the correct type.
     */
    default void castRead(I instance, Tag tag) {
        //noinspection unchecked
        read(instance, (T) tag);
    }

    /**
     * Reads a tag into a value in the instance.
     * @param instance the instance to read the tag into.
     * @param tag the tag to read.
     */
    void read(I instance, T tag);

    /**
     * Writes a value from the instance into a tag.
     * @param instance the instance to write.
     * @return the tag that was written.
     */
    T write(I instance);
}
