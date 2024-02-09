package com.teamresourceful.resourcefullib.common.comptibility.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.comptibility.CompatabilityOptions;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record AndOption(
        List<CompatabilityOption<?>> options, Component title, List<Component> description, String url
) implements CompatabilityOption<AndOption> {

    public static final Codec<AndOption> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CompatabilityOptions.CODEC.listOf().fieldOf("options").forGetter(AndOption::options),
            ExtraCodecs.COMPONENT.optionalFieldOf("title", CommonComponents.EMPTY).forGetter(AndOption::title),
            ExtraCodecs.COMPONENT.listOf().optionalFieldOf("description", List.of()).forGetter(AndOption::description),
            Codec.STRING.optionalFieldOf("url", "").forGetter(AndOption::url)
    ).apply(instance, AndOption::new));
    public static final String ID = "and";

    @Override
    public Codec<AndOption> codec() {
        return CODEC;
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean matches() {
        for (CompatabilityOption<?> option : this.options) {
            if (!option.matches()) {
                return false;
            }
        }
        return true;
    }
}
