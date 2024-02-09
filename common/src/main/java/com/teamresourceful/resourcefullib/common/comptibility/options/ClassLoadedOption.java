package com.teamresourceful.resourcefullib.common.comptibility.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record ClassLoadedOption(
        String clazz, Component title, List<Component> description, String url
) implements CompatabilityOption<ClassLoadedOption> {

    public static final Codec<ClassLoadedOption> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("class").forGetter(ClassLoadedOption::clazz),
            ExtraCodecs.COMPONENT.optionalFieldOf("title", CommonComponents.EMPTY).forGetter(ClassLoadedOption::title),
            ExtraCodecs.COMPONENT.listOf().optionalFieldOf("description", List.of()).forGetter(ClassLoadedOption::description),
            Codec.STRING.optionalFieldOf("url", "").forGetter(ClassLoadedOption::url)
    ).apply(instance, ClassLoadedOption::new));
    public static final String ID = "class_loaded";

    @Override
    public Codec<ClassLoadedOption> codec() {
        return CODEC;
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean matches() {
        try {
            Class.forName(clazz);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
