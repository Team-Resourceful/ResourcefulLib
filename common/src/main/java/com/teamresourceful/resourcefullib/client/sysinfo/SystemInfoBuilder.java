package com.teamresourceful.resourcefullib.client.sysinfo;

import com.mojang.datafixers.util.Pair;

import java.util.ArrayList;
import java.util.List;

public record SystemInfoBuilder(String category, List<Pair<String, String>> info) {

    public SystemInfoBuilder(String category) {
        this(category, new ArrayList<>());
    }

    public SystemInfoBuilder append(String key, Object value) {
        info.add(Pair.of(key, value.toString()));
        return this;
    }

    public SystemInfoBuilder append(String key, String value, Object... args) {
        info.add(Pair.of(key, String.format(value, args)));
        return this;
    }

}
