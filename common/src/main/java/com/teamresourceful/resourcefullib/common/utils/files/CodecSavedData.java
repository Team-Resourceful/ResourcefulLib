package com.teamresourceful.resourcefullib.common.utils.files;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.slf4j.Logger;

import java.util.function.Supplier;

public final class CodecSavedData<T> extends SavedData implements Supplier<T> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Factory<T> factory;
    private T data;

    private CodecSavedData(Factory<T> factory, T data) {
        this.factory = factory;
        this.data = data;
    }

    private CodecSavedData(Factory<T> factory) {
        this.factory = factory;
        this.data = factory.defaultValue.get();
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

    public static <T> Factory<T> create(Codec<T> codec, Identifier id) {
        return new Factory<>(codec, id);
    }

    public static class Factory<T> {

        private final Codec<T> codec;
        private final Identifier id;

        private Supplier<T> defaultValue = () -> null;
        private boolean alwaysDirty = false;
        private boolean global = false;

        private SavedDataType<CodecSavedData<T>> type;

        private Factory(Codec<T> codec, Identifier id) {
            this.codec = codec;
            this.id = id;
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

        public CodecSavedData<T> create(ServerLevel level) {
            var storage = this.global ? level.getServer().overworld().getDataStorage() : level.getDataStorage();
            if (this.type == null) {
                // https://github.com/neoforged/NeoForge/blob/1.21.x/patches/net/minecraft/world/level/storage/DimensionDataStorage.java.patch
                // https://github.com/FabricMC/fabric/blob/1.21.4/fabric-object-builder-api-v1/src/main/java/net/fabricmc/fabric/mixin/object/builder/PersistentStateManagerMixin.java
                this.type = new SavedDataType<>(
                        this.id,
                        () -> new CodecSavedData<>(this),
                        codec.xmap(data -> new CodecSavedData<>(this, data), data -> data.data),
                        null
                );
            }
            return storage.computeIfAbsent(this.type);
        }
    }
}
