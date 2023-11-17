package com.teamresourceful.resourcefullib.common.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class SaveHandler extends SavedData {

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        this.saveData(tag);
        return tag;
    }

    public abstract void loadData(CompoundTag tag);

    public abstract void saveData(CompoundTag tag);

    public static <T extends SaveHandler> T read(Level level, HandlerType<T> type, String id) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return type.clientSide();
        }
        return read(serverLevel.getServer().overworld().getDataStorage(), type, id);
    }

    public static <T extends SaveHandler> T read(DimensionDataStorage storage, HandlerType<T> factory, String id) {
        return storage.computeIfAbsent(factory.factory(), id);
    }

    public static <T extends SaveHandler> void handle(Level level, Function<Level, T> getter, Consumer<T> operation) {
        T handler = getter.apply(level);
        operation.accept(handler);
        handler.setDirty();
    }

    public record HandlerType<T extends SaveHandler>(@Nullable T clientSide, Factory<T> factory) {

        public static <T extends SaveHandler> HandlerType<T> create(Supplier<T> creator) {
            return create(null, creator);
        }

        public static <T extends SaveHandler> HandlerType<T> create(T clientSide, Supplier<T> creator) {
            return new HandlerType<>(clientSide, new Factory<>(creator, tag -> {
                T handler = creator.get();
                handler.loadData(tag);
                return handler;
            }, DataFixTypes.OPTIONS));
            //TODO minecraft forge is dumb so useless datafixer is needed, fabric and neo both allow null
        }

    }

}