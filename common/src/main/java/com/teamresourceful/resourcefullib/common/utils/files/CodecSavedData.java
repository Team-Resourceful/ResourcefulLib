package com.teamresourceful.resourcefullib.common.utils.files;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public final class CodecSavedData<T> extends SavedData implements Supplier<T> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Factory<T> factory;
    private T data;

    private CodecSavedData(Factory<T> factory, CompoundTag tag, HolderLookup.Provider provider) {
        this.factory = factory;
        this.data = factory.codec.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag)
                .ifError(e -> LOGGER.error("Failed to parse data for {}", factory.path, e))
                .result()
                .orElseGet(factory.defaultValue);
    }

    private CodecSavedData(Factory<T> factory) {
        this.factory = factory;
        this.data = factory.defaultValue.get();
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        this.factory.codec
                .encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), this.get())
                .ifError(e -> LOGGER.error("Failed to encode data for {}", this.factory.path, e))
                .result()
                .ifPresent(compound -> {
                    if (compound instanceof CompoundTag compoundTag) {
                        if (this.factory.clean) {
                            Set<String> keys = new HashSet<>(tag.getAllKeys());
                            keys.forEach(tag::remove);
                        }
                        tag.merge(compoundTag);
                    } else {
                        LOGGER.error("Codec {} did not return a CompoundTag for {}", this.factory.codec, this.factory.path);
                    }
                });
        return tag;
    }

    @Override
    public boolean isDirty() {
        return this.factory.alwaysDirty || super.isDirty();
    }

    @Override
    public T get() {
        return this.data;
    }

    public void set(T data) {
        this.data = data;
        this.setDirty();
    }

    public static <T> Factory<T> create(Codec<T> codec, Supplier<T> supplier, String path) {
        return new Factory<>(codec, supplier, path);
    }

    public static class Factory<T> {

        private final Codec<T> codec;
        private final String path;

        private Supplier<T> defaultValue = () -> null;
        private boolean alwaysDirty = false;
        private boolean global = false;
        private boolean clean = false;

        private SavedData.Factory<CodecSavedData<T>> factory;

        private Factory(Codec<T> codec, Supplier<T> supplier, String path) {
            this.codec = codec;
            this.path = path;
        }

        /**
         * Sets the default value for the data.
         * @param defaultValue The default value for the data.
         */
        public Factory<T> defaultValue(Supplier<T> defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        /**
         * Forces the data to be saved every save.
         */
        public Factory<T> alwaysDirty() {
            this.alwaysDirty = true;
            return this;
        }

        /**
         * Saves the data to the global data storage.
         */
        public Factory<T> global() {
            this.global = true;
            return this;
        }

        /**
         * Removes all tags from the saved data before saving.
         */
        public Factory<T> clean() {
            this.clean = true;
            return this;
        }

        public CodecSavedData<T> create(ServerLevel level) {
            DimensionDataStorage storage = this.global ? level.getServer().overworld().getDataStorage() : level.getDataStorage();
            if (this.factory == null) {
                this.factory = new SavedData.Factory<>(
                        () -> new CodecSavedData<>(this),
                        (tag, provider) -> new CodecSavedData<>(this, tag, provider),
                        null
                );
            }
            return storage.computeIfAbsent(this.factory, this.path);
        }
    }
}
