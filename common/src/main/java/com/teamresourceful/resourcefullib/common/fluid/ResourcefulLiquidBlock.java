package com.teamresourceful.resourcefullib.common.fluid;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import net.minecraft.world.level.block.LiquidBlock;

public class ResourcefulLiquidBlock extends LiquidBlock {

    public ResourcefulLiquidBlock(FluidData data, Properties properties) {
        super(data.flowing().get(), properties);
        data.setBlock(() -> this);
    }
}
