package com.teamresourceful.resourcefullib.common.comptibility;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.comptibility.options.*;

import java.util.HashMap;
import java.util.Map;

public class CompatabilityOptions {

    private static final Map<String, Codec<CompatabilityOption<?>>> OPTIONS = new HashMap<>();

    public static final Codec<CompatabilityOption<?>> CODEC = Codec.STRING.dispatch(CompatabilityOption::id, OPTIONS::get);

    public static void init() {
        registerOption(AndOption.ID, AndOption.CODEC);
        registerOption(OrOption.ID, OrOption.CODEC);
        registerOption(ClassLoadedOption.ID, ClassLoadedOption.CODEC);
        registerOption(ModLoadedOption.ID, ModLoadedOption.CODEC);
        registerOption(NotOption.ID, NotOption.CODEC);
        registerOption(IsClientOption.ID, IsClientOption.CODEC);
    }

    @SuppressWarnings("unchecked")
    public static <T extends CompatabilityOption<T>> void registerOption(String id, Codec<T> codec) {
        OPTIONS.put(id, (Codec<CompatabilityOption<?>>) codec);
    }
}
