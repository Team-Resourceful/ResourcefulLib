package com.teamresourceful.resourcefullib.common.fluid.data;

import net.minecraft.sounds.SoundEvent;

import java.util.HashMap;
import java.util.Map;

public record FluidSounds(Map<String, SoundEvent> sounds) {

    public static final String BUCKET_EMPTY = "bucket_empty";
    public static final String BUCKET_FILL = "bucket_fill";

    public FluidSounds() {
        this(new HashMap<>());
    }

    public void add(String name, SoundEvent sound) {
        sounds.put(name, sound);
    }

    public SoundEvent getOrDefault(String name, SoundEvent defaultSound) {
        return sounds.getOrDefault(name, defaultSound);
    }

    public boolean has(String name) {
        return sounds.containsKey(name);
    }


}
