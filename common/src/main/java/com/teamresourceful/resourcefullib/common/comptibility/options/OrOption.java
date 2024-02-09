package com.teamresourceful.resourcefullib.common.comptibility.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.comptibility.CompatabilityOptions;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record OrOption(
        List<CompatabilityOption<?>> options, Component title, List<Component> description, String url
) implements CompatabilityOption<OrOption> {

    public static final Codec<OrOption> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CompatabilityOptions.CODEC.listOf().fieldOf("options").forGetter(OrOption::options),
            ExtraCodecs.COMPONENT.optionalFieldOf("title", CommonComponents.EMPTY).forGetter(OrOption::title),
            ExtraCodecs.COMPONENT.listOf().optionalFieldOf("description", List.of()).forGetter(OrOption::description),
            Codec.STRING.optionalFieldOf("url", "").forGetter(OrOption::url)
    ).apply(instance, OrOption::new));
    public static final String ID = "or";

    @Override
    public Codec<OrOption> codec() {
        return CODEC;
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean matches() {
        for (CompatabilityOption<?> option : this.options) {
            if (option.matches()) {
                return true;
            }
        }
        return false;
    }
}
