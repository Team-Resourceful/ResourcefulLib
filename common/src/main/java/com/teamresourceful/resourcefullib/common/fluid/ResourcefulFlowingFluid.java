package com.teamresourceful.resourcefullib.common.fluid;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public abstract class ResourcefulFlowingFluid extends FlowingFluid {

    private final FluidData data;

    public ResourcefulFlowingFluid(FluidData data) {
        this.data = data;
    }

    @ApiStatus.Internal
    public FluidData getData() {
        return this.data;
    }

    @Override
    public @NotNull Fluid getFlowing() {
        return this.data.flowing().get();
    }

    @Override
    public @NotNull Fluid getSource() {
        return this.data.still().get();
    }

    @Override
    protected boolean canConvertToSource(Level level) {
        return this.data.properties().canConvertToSource();
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        Block.dropResources(state, level, pos, state.hasBlockEntity() ? level.getBlockEntity(pos) : null);
    }

    @Override
    protected int getSlopeFindDistance(LevelReader levelReader) {
        return this.data.properties().slopeFindDistance();
    }

    @Override
    protected int getDropOff(LevelReader levelReader) {
        return this.data.properties().dropOff();
    }

    @Override
    public @NotNull Item getBucket() {
        return Objects.requireNonNullElse(this.data.bucket().get(), Items.AIR);
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !isSame(fluid);
    }

    @Override
    public int getTickDelay(LevelReader levelReader) {
        return this.data.properties().tickDelay();
    }

    @Override
    protected float getExplosionResistance() {
        return this.data.properties().explosionResistance();
    }

    @Override
    protected @NotNull BlockState createLegacyBlock(FluidState fluidState) {
        Block block = this.data.block().get();
        if (block == null) return Blocks.AIR.defaultBlockState();
        return block.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(fluidState));
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == this.data.still().get() || fluid == this.data.flowing().get();
    }

    @Override
    public @NotNull Optional<SoundEvent> getPickupSound() {
        return Optional.of(this.data.properties().sounds().getOrDefault(FluidSounds.BUCKET_FILL, SoundEvents.BUCKET_FILL));
    }

    public static class Flowing extends ResourcefulFlowingFluid {

        public Flowing(FluidData data) {
            super(data);
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
            data.setFlowing(() -> this);
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }


        @Override
        public boolean isSource(FluidState state) {
            return false;
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }
    }

    public static class Still extends ResourcefulFlowingFluid {

        public Still(FluidData data) {
            super(data);
            data.setStill(() -> this);
        }

        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }


}
