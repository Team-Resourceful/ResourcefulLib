package com.teamresourceful.resourcefullib.common.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamresourceful.resourcefullib.common.lib.Constants;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public abstract class GenericMemoryPack implements PackResources {

    private final Map<ResourceLocation, IoSupplier<InputStream>> data = new HashMap<>();

    private final JsonObject metaData;
    private final PackType allowedType;
    private final String id;
    private final PackLocationInfo info;

    protected GenericMemoryPack(PackType type, String id, JsonObject meta) {
        this.metaData = meta;
        this.allowedType = type;
        this.id = id;

        this.info = new PackLocationInfo(id, CommonComponents.EMPTY, PackSource.BUILT_IN, Optional.empty());
    }

    private boolean isTypeAllowed(PackType type) {
        return allowedType.equals(type);
    }

    public void putData(PackType type, ResourceLocation location, IoSupplier<InputStream> supplier){
        if (!isTypeAllowed(type)) return;
        data.put(location, supplier);
    }

    public void putJson(PackType type, ResourceLocation location, JsonElement json) {
        putData(type, location, () -> new ByteArrayInputStream(Constants.GSON.toJson(json).getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public @Nullable IoSupplier<InputStream> getRootResource(String @NotNull ... files) {
        String file = String.join("/", files);
        if(file.contains("/") || file.contains("\\")) {
            throw new IllegalArgumentException("Root resources can only be filenames, not paths (no / allowed!)");
        }
        return null;
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(@NotNull PackType type, @NotNull ResourceLocation location) {
        if (!isTypeAllowed(type)) return null;
        return this.data.getOrDefault(location, null);
    }

    @Override
    public void listResources(@NotNull PackType type, @NotNull String namespace, @NotNull String path, @NotNull ResourceOutput output) {
        if (!isTypeAllowed(type)) return;
        this.data.entrySet().stream()
            .filter(entry -> entry.getKey().getNamespace().equals(namespace))
            .filter(entry -> entry.getKey().getPath().startsWith(path))
            .forEach(entry -> output.accept(entry.getKey(), entry.getValue()));
    }

    @Override
    public @NotNull Set<String> getNamespaces(@NotNull PackType type) {
        if (!isTypeAllowed(type)) return Collections.emptySet();
        return data.keySet().stream().map(ResourceLocation::getNamespace).collect(Collectors.toSet());
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(@NotNull MetadataSectionSerializer<T> serializer) {
        if (!serializer.getMetadataSectionName().equals("pack")) return null;
        return serializer.fromJson(metaData);
    }

    @Override
    public @NotNull String packId() {
        return this.id;
    }

    @Override
    public @NotNull PackLocationInfo location() {
        return this.info;
    }

    @Override
    public void close() {
        for (IoSupplier<InputStream> value : data.values()) {
            try {
                value.get().close();
            } catch (IOException e) {
                Constants.LOGGER.error("Failed to close input stream", e);
            }
        }
    }
}

