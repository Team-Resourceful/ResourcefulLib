package com.teamresourceful.resourcefullib.common.nbt;

import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class TagUtils {

    private TagUtils() throws UtilityClassException {
        throw new UtilityClassException();
    }

    public static CompoundTag tagWithData(String key, Tag tag) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put(key, tag);
        return compoundTag;
    }

    public static <T extends Tag> ListTag toListTag(@Nullable List<T> tags) {
        ListTag list = new ListTag();
        if (tags != null) list.addAll(tags);
        return list;
    }

    public static <T extends Tag> List<T> fromListTag(ListTag list, Class<T> tagClass) {
        return list.stream().filter(tagClass::isInstance).map(tagClass::cast).toList();
    }

    public static <K, V> Map<K, V> mapTag(CompoundTag tag, Function<String, K> keyMapper, BiFunction<String, CompoundTag, V> valueMapper) {
        Map<K, V> map = new HashMap<>();
        for (String key : tag.getAllKeys()) {
            map.put(keyMapper.apply(key), valueMapper.apply(key, tag));
        }
        return map;
    }

    public static <C extends Collection<T>, T> C mapToCollection(Supplier<C> collection, ListTag list, Function<Tag, T> mapper) {
        C c = collection.get();
        for (Tag tag : list) {
            c.add(mapper.apply(tag));
        }
        return c;
    }

    public static <T> ListTag mapToListTag(Collection<T> collection, Function<T, Tag> mapper) {
        ListTag list = new ListTag();
        for (T t : collection) {
            list.add(mapper.apply(t));
        }
        return list;
    }
}
