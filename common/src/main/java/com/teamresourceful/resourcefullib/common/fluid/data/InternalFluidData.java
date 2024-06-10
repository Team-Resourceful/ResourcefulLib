package com.teamresourceful.resourcefullib.common.fluid.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

/**
 * @hidden Internal class used to store data about fluids.
 */
@ApiStatus.Internal
public class InternalFluidData implements FluidData {

    private Supplier<? extends FlowingFluid> still;
    private Supplier<? extends FlowingFluid> flowing;
    private Supplier<? extends Item> bucket;
    private Supplier<? extends LiquidBlock> block;

    private final ResourceLocation id;
    private final FluidProperties properties;
    private final Supplier<?> data;

    public InternalFluidData(ResourceLocation id, FluidProperties properties, Supplier<?> data) {
        this.id = id;
        this.properties = properties;
        this.data = data;
    }

    public InternalFluidData(ResourceLocation id, FluidProperties properties) {
        this(id, properties, () -> null);
    }

    @Override
    public ResourceLocation id() {
        return this.id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T data() {
        return (T) this.data.get();
    }

    @Override
    public FluidProperties properties() {
        return this.properties;
    }

    @Override
    public Supplier<? extends FlowingFluid> still() {
        return this.still;
    }

    @Override
    public Supplier<? extends FlowingFluid> flowing() {
        return this.flowing;
    }

    @Override
    public Supplier<? extends Item> bucket() {
        return this.bucket;
    }

    @Override
    public Supplier<? extends LiquidBlock> block() {
        return this.block;
    }

    @Override
    public void setStill(Supplier<? extends FlowingFluid> still) {
        this.still = still;
    }

    @Override
    public void setFlowing(Supplier<? extends FlowingFluid> flowing) {
        this.flowing = flowing;
    }

    @Override
    public void setBucket(Supplier<? extends Item> bucket) {
        this.bucket = bucket;
    }

    @Override
    public void setBlock(Supplier<? extends LiquidBlock> block) {
        this.block = block;
    }
}