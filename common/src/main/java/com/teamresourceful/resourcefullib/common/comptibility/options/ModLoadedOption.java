package com.teamresourceful.resourcefullib.common.comptibility.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfoUtils;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record ModLoadedOption(
        String mod, Component title, List<Component> description, String url
) implements CompatabilityOption<ModLoadedOption> {

    public static final Codec<ModLoadedOption> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("mod").forGetter(ModLoadedOption::mod),
            ExtraCodecs.COMPONENT.optionalFieldOf("title", CommonComponents.EMPTY).forGetter(ModLoadedOption::title),
            ExtraCodecs.COMPONENT.listOf().optionalFieldOf("description", List.of()).forGetter(ModLoadedOption::description),
            Codec.STRING.optionalFieldOf("url", "").forGetter(ModLoadedOption::url)
    ).apply(instance, ModLoadedOption::new));
    public static final String ID = "mod_loaded";

    @Override
    public Codec<ModLoadedOption> codec() {
        return CODEC;
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean matches() {
        return ModInfoUtils.isModLoaded(mod);
    }
}
