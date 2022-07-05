package com.teamresourceful.resourcefullib.common.codecs.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record RestrictedBlockPredicate(@NotNull Block block, @NotNull NbtPredicate nbt, @NotNull LocationPredicate location, @NotNull StatePropertiesPredicate properties) {

    public static final Codec<RestrictedBlockPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Registry.BLOCK.byNameCodec().fieldOf("id").forGetter(RestrictedBlockPredicate::block),
            NbtPredicate.CODEC.fieldOf("nbt").orElse(NbtPredicate.ANY).forGetter(RestrictedBlockPredicate::nbt),
            CodecExtras.passthrough(LocationPredicate::serializeToJson, LocationPredicate::fromJson).fieldOf("location").orElse(LocationPredicate.ANY).forGetter(RestrictedBlockPredicate::location),
            CodecExtras.passthrough(StatePropertiesPredicate::serializeToJson, StatePropertiesPredicate::fromJson).fieldOf("properties").orElse(StatePropertiesPredicate.ANY).forGetter(RestrictedBlockPredicate::properties)
    ).apply(instance, RestrictedBlockPredicate::new));

    public Optional<CompoundTag> getTag() {
        return Optional.ofNullable(nbt().tag());
    }

    public boolean matches(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!state.is(this.block)) {
            return false;
        } else if (!this.properties.matches(state)) {
            return false;
        } else if (!this.location.matches(level, pos.getX(), pos.getY(), pos.getZ())) {
            return false;
        } else  if (this.nbt != NbtPredicate.ANY) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            return blockEntity != null && this.nbt.matches(blockEntity.saveWithFullMetadata());
        }
        return true;
    }
}
