package com.teamresourceful.resourcefullib.common.fluid.neoforge;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidProperties;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.SoundAction;
import net.neoforged.neoforge.fluids.FluidType;

public class ResourcefulFluidType extends FluidType {

    public ResourcefulFluidType(ResourceLocation id, FluidProperties props) {
        super(Util.make(Properties.create(), properties -> {
            properties.descriptionId(Util.makeDescriptionId("fluid_type", id));
            properties.adjacentPathType(props.adjacentPathType());
            properties.canConvertToSource(props.canConvertToSource());
            properties.canDrown(props.canDrown());
            properties.canExtinguish(props.canExtinguish());
            properties.canHydrate(props.canHydrate());
            properties.canPushEntity(props.canPushEntity());
            properties.canSwim(props.canSwim());
            properties.density(props.density());
            properties.fallDistanceModifier(props.fallDistanceModifier());
            properties.lightLevel(props.lightLevel());
            properties.motionScale(props.motionScale());
            properties.pathType(props.pathType());
            properties.rarity(props.rarity());
            properties.temperature(props.temperature());
            properties.viscosity(props.viscosity());
            props.sounds().sounds().forEach((name, sound) -> properties.sound(SoundAction.get(name), sound));
        }));
    }
}
