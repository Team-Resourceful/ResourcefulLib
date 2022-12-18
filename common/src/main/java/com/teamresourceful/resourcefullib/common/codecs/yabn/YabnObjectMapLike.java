package com.teamresourceful.resourcefullib.common.codecs.yabn;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapLike;
import com.teamresourceful.yabn.elements.YabnElement;
import com.teamresourceful.yabn.elements.YabnObject;
import com.teamresourceful.yabn.elements.YabnPrimitive;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class YabnObjectMapLike implements MapLike<YabnElement> {

    private final YabnObject object;

    public YabnObjectMapLike(YabnObject yabnObject) {
        this.object = yabnObject;
    }

    @Nullable
    @Override
    public YabnElement get(YabnElement key) {
        if (key instanceof YabnPrimitive primitive) return get(YabnOps.getAsString(primitive));
        throw new IllegalArgumentException("Not a string: " + key);
    }

    @Nullable
    @Override
    public YabnElement get(String key) {
        YabnElement element = object.elements().get(key);
        return element == null ? null : element.getOrNull();
    }

    @Override
    public Stream<Pair<YabnElement, YabnElement>> entries() {
        return object.elements().entrySet().stream().map(entry -> Pair.of(YabnPrimitive.ofString(entry.getKey()), entry.getValue()));
    }
}
