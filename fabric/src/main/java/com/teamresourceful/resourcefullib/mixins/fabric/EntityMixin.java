package com.teamresourceful.resourcefullib.mixins.fabric;

import com.teamresourceful.resourcefullib.client.fluid.fabric.EntityFluidEyesHook;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityFluidEyesHook {

    @Shadow public abstract double getEyeY();

    @Shadow public abstract double getX();

    @Shadow public abstract double getZ();

    @Shadow public abstract Level level();

    @Unique
    private FluidState rlibEyesFluid;

    @Inject(method = "updateFluidOnEyes", at = @At("HEAD"))
    private void rlib_resetEyesFluid(CallbackInfo ci) {
        rlibEyesFluid = null;
        BlockPos blockPos = BlockPos.containing(this.getX(), this.getEyeY(), this.getZ());
        FluidState fluidState = this.level().getFluidState(blockPos);
        double e = (float)blockPos.getY() + fluidState.getHeight(this.level(), blockPos);
        if (e > this.getEyeY()) {
            rlibEyesFluid = fluidState;
        }
    }

    @Override
    public FluidState rlib$getEyesFluid() {
        return rlibEyesFluid;
    }
}
