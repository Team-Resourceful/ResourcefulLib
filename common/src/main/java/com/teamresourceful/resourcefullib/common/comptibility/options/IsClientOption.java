package com.teamresourceful.resourcefullib.common.comptibility.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.comptibility.CompatabilityManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record IsClientOption(
        Component title, List<Component> description, String url
) implements CompatabilityOption<IsClientOption> {

    public static final Codec<IsClientOption> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.COMPONENT.optionalFieldOf("title", CommonComponents.EMPTY).forGetter(IsClientOption::title),
            ExtraCodecs.COMPONENT.listOf().optionalFieldOf("description", List.of()).forGetter(IsClientOption::description),
            Codec.STRING.optionalFieldOf("url", "").forGetter(IsClientOption::url)
    ).apply(instance, IsClientOption::new));
    public static final String ID = "is_client";

    @Override
    public Codec<IsClientOption> codec() {
        return CODEC;
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean matches() {
        return CompatabilityManager.isClient();
    }
}
