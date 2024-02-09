package com.teamresourceful.resourcefullib.common.comptibility.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.comptibility.CompatabilityOptions;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record NotOption(
        CompatabilityOption<?> option, Component title, List<Component> description, String url
) implements CompatabilityOption<NotOption> {

    public static final Codec<NotOption> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CompatabilityOptions.CODEC.fieldOf("options").forGetter(NotOption::option),
            ExtraCodecs.COMPONENT.optionalFieldOf("title", CommonComponents.EMPTY).forGetter(NotOption::title),
            ExtraCodecs.COMPONENT.listOf().optionalFieldOf("description", List.of()).forGetter(NotOption::description),
            Codec.STRING.optionalFieldOf("url", "").forGetter(NotOption::url)
    ).apply(instance, NotOption::new));
    public static final String ID = "not";

    @Override
    public Codec<NotOption> codec() {
        return CODEC;
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean matches() {
        return !option.matches();
    }
}
