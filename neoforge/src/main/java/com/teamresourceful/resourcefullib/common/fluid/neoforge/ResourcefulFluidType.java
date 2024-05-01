package com.teamresourceful.resourcefullib.common.fluid.neoforge;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidProperties;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundAction;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ResourcefulFluidType extends FluidType {

    private final FluidProperties properties;

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
        this.properties = props;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        var type = this;
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public @NotNull ResourceLocation getStillTexture() {
                return type.properties.still();
            }

            @Override
            public @NotNull ResourceLocation getFlowingTexture() {
                return type.properties.flowing();
            }

            @Override
            public @Nullable ResourceLocation getOverlayTexture() {
                return type.properties.overlay();
            }

            @Override
            public @Nullable ResourceLocation getRenderOverlayTexture(@NotNull Minecraft mc) {
                return type.properties.screenOverlay();
            }

            @Override
            public int getTintColor() {
                return type.properties.tintColor();
            }
        });
    }
}
