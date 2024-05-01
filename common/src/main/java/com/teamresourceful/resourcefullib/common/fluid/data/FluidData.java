package com.teamresourceful.resourcefullib.common.fluid.data;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

public interface FluidData {

    FluidProperties properties();

    Supplier<? extends FlowingFluid> still();

    Supplier<? extends FlowingFluid> flowing();

    Supplier<? extends Item> bucket();

    Supplier<? extends LiquidBlock> block();

    /** @hidden This method is an internal API. */
    @ApiStatus.Internal
    <T> T data();

    /** @hidden This method is an internal API. */
    @ApiStatus.Internal
    void setStill(Supplier<? extends FlowingFluid> still);

    /** @hidden This method is an internal API. */
    @ApiStatus.Internal
    void setFlowing(Supplier<? extends FlowingFluid> flowing);

    /** @hidden This method is an internal API. */
    @ApiStatus.Internal
    void setBucket(Supplier<? extends Item> bucket);

    /** @hidden This method is an internal API. */
    @ApiStatus.Internal
    void setBlock(Supplier<? extends LiquidBlock> block);
}
