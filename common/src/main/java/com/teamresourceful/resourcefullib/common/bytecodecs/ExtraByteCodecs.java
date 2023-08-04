package com.teamresourceful.resourcefullib.common.bytecodecs;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.Optional;

public final class ExtraByteCodecs {

    private ExtraByteCodecs() throws UtilityClassException {
        throw new UtilityClassException();
    }

    public static final ByteCodec<ResourceLocation> RESOURCE_LOCATION = ByteCodec.STRING.map(ResourceLocation::new, ResourceLocation::toString);
    public static final ByteCodec<ResourceKey<Level>> DIMENSION = resourceKey(Registries.DIMENSION);

    public static final ByteCodec<BlockPos> BLOCK_POS = ByteCodec.LONG.map(BlockPos::of, BlockPos::asLong);
    public static final ByteCodec<ChunkPos> CHUNK_POS = ByteCodec.LONG.map(ChunkPos::new, ChunkPos::toLong);
    public static final ByteCodec<SectionPos> SECTION_POS = ByteCodec.LONG.map(SectionPos::of, SectionPos::asLong);
    public static final ByteCodec<GlobalPos> GLOBAL_POS = ObjectByteCodec.create(
            DIMENSION.fieldOf(GlobalPos::dimension),
            BLOCK_POS.fieldOf(GlobalPos::pos),
            GlobalPos::of
    );
    public static final ByteCodec<Vector3f> VECTOR_3F = ObjectByteCodec.create(
            ByteCodec.FLOAT.fieldOf(Vector3f::x),
            ByteCodec.FLOAT.fieldOf(Vector3f::y),
            ByteCodec.FLOAT.fieldOf(Vector3f::z),
            Vector3f::new
    );

    public static final ByteCodec<CompoundTag> NULLABLE_COMPOUND_TAG = CompoundTagByteCodec.INSTANCE
            .map(value -> value.orElse(null), Optional::ofNullable);
    public static final ByteCodec<CompoundTag> NONNULL_COMPOUND_TAG = CompoundTagByteCodec.INSTANCE
            .map(Optional::orElseThrow, Optional::of);
    public static final ByteCodec<Optional<CompoundTag>> COMPOUND_TAG = CompoundTagByteCodec.INSTANCE;

    public static final ByteCodec<Component> COMPONENT = ByteCodec.STRING_COMPONENT
            .map(Component.Serializer::fromJson, Component.Serializer::toJson);

    public static final ByteCodec<Item> ITEM = registry(BuiltInRegistries.ITEM);
    public static final ByteCodec<ItemStack> ITEM_STACK = ItemStackByteCodec.CODEC;


    public static <T, R extends Registry<T>> ByteCodec<ResourceKey<T>> resourceKey(ResourceKey<R> registry) {
        return RESOURCE_LOCATION.map(id -> ResourceKey.create(registry, id), ResourceKey::location);
    }

    public static <T> ByteCodec<T> registry(IdMap<T> map) {
        return new IdMapByteCodec<>(map);
    }
}
