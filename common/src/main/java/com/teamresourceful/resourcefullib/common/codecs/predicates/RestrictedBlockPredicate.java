package com.teamresourceful.resourcefullib.common.codecs.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.predicates.properties.BlockStatePredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record RestrictedBlockPredicate(@NotNull Block block, Optional<NbtPredicate> nbt, Optional<LocationPredicate> location, @NotNull BlockStatePredicate properties) {

    public static final Codec<RestrictedBlockPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("id").forGetter(RestrictedBlockPredicate::block),
            ExtraCodecs.strictOptionalField(NbtPredicate.CODEC, "nbt").forGetter(RestrictedBlockPredicate::nbt),
            ExtraCodecs.strictOptionalField(LocationPredicate.CODEC, "location").forGetter(RestrictedBlockPredicate::location),
            BlockStatePredicate.CODEC.fieldOf("properties").orElse(BlockStatePredicate.ANY).forGetter(RestrictedBlockPredicate::properties)
    ).apply(instance, RestrictedBlockPredicate::new));

    public Optional<CompoundTag> getTag() {
        return nbt().map(NbtPredicate::tag);
    }

    public boolean matches(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!state.is(this.block)) {
            return false;
        } else if (!this.properties.matches(state)) {
            return false;
        } else if (this.location.isPresent() && !this.location.get().matches(level, pos.getX(), pos.getY(), pos.getZ())) {
            return false;
        } else  if (this.nbt.isPresent()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            return blockEntity != null && this.nbt.get().matches(blockEntity.saveWithFullMetadata());
        }
        return true;
    }
}
