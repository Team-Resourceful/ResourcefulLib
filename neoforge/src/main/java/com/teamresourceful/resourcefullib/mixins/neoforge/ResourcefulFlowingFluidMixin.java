package com.teamresourceful.resourcefullib.mixins.neoforge;

import com.teamresourceful.resourcefullib.common.fluid.ResourcefulFlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ResourcefulFlowingFluid.class)
public abstract class ResourcefulFlowingFluidMixin extends Fluid {

    @Unique
    private FluidType rlib$fluidType = null;

    @Override
    public @NotNull FluidType getFluidType() {
        if (this.rlib$fluidType == null) {
            //noinspection DataFlowIssue
            ResourcefulFlowingFluid fluid = (ResourcefulFlowingFluid) (Object) this;
            this.rlib$fluidType = fluid.getData().data();
            if (this.rlib$fluidType == null) {
                throw new IllegalStateException("FluidType is null for fluid: " + fluid);
            }
        }
        return this.rlib$fluidType;
    }
}
