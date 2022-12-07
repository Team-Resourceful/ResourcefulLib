package com.teamresourceful.resourcefullib.common.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class TagUtils {

    private TagUtils() {}

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


}
