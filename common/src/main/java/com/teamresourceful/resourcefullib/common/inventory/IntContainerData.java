package com.teamresourceful.resourcefullib.common.inventory;

import net.minecraft.world.inventory.ContainerData;

/**
 * An implementation of vanillas ContainerData that splits integers into two separate shorts.
 * <p>
 * ClientBoundContainerSetDataPacket serializes ints into shorts causing loss of data when the
 * int is outside the range of -32768 and 32767. IntContainerData doubles the array size and
 * stores the values as shorts making the data compatible with vanillas networking.
 * <p>
 * The Set and Get methods are made final and marked as deprecated to maintain compatibility,
 * instead being replaced by setInt and getInt. getCount is also made final and marked as
 * deprecated with size() as its replacement.
 * <p>
 * These new methods take into account the doubled array size so that standard indexing can be used
 * the same as it would with ContainerData and SimpleContainerData.
 */
public class IntContainerData implements ContainerData {

    private final short[] data;

    /**
     * Creates a new Container Data object that automatically splits integers
     * into shorts for networking compatibility.
     * <p>
     * On creation of a new IntContainerData the size passed will automatically be doubled
     * to account for the integer split.
     *
     * @param size The number of values being stored
     */
    public IntContainerData(int size) {
        this.data = new short[size * 2];
    }

    /**
     * This only exists for compatibility with vanilla code.
     * SetInt(...) should be used in place of this!
     */
    @Override
    @Deprecated
    public final void set(int index, int value) {
        data[index] = (short) value;
    }

    /**
     * This method will bit shift the value passed in so that it is
     * stored as two Shorts. This makes it easier to use integers greater than
     * 32767 since vanilla casts ints to shorts in ClientBoundContainerSetDataPacket
     *
     * @param index The index based on the size passed in when the object was created
     * @param value The value being stored
     */
    public void setInt(int index, int value) {
        int i = index * 2;
        set(i, value >> 16);
        set(i + 1, value & 0xffff);
    }

    /**
     * This only exists for compatibility with vanilla code.
     * GetInt(...) should be used in place of this!
     */
    @Override
    @Deprecated
    public final int get(int index) {
        return data[index];
    }

    /**
     * This method will bit shift the stored short values so that it returns the
     * represented integer value.
     * This makes it easier to use integers greater than
     * 32767 since vanilla casts ints to shorts in ClientBoundContainerSetDataPacket
     *
     * @param index The index based on the size passed in when the object was created.
     */
    public int getInt(int index) {
        int i = index * 2;
        return (get(i) << 16) | (get(i + 1) & 0xffff);
    }

    /**
     * This exists for compatibility with vanilla. This will return
     * the length of the internal array and does not alter the return
     * value to account for the initial size passed on creation.
     * <p>
     * Use size() if you would like to account for the size passed in
     * when the object was created.
     *
     * @return The length of the internal array.
     */
    @Override
    @Deprecated
    public final int getCount() {
        return data.length;
    }

    /**
     * Use this if you would like to account for the size passed in
     * when the object was created. This divides the length of the internal array
     * by 2.
     *
     * @return The size that was passed in when the object was created.
     */
    public final int size() {
        return data.length / 2;
    }
}
