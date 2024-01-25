package com.teamresourceful.resourcefullib.common.module.neoforge;

import com.teamresourceful.resourcefullib.common.modules.Module;
import com.teamresourceful.resourcefullib.common.modules.ModuleType;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;

public record ModuleSerializer<T extends Module<T>>(ModuleType<T> type) implements IAttachmentSerializer<CompoundTag, T> {

    @Override
    public T read(IAttachmentHolder holder, CompoundTag tag) {
        T module = type.create();
        module.read(tag);
        return module;
    }

    @Override
    public CompoundTag write(T module) {
        CompoundTag tag = new CompoundTag();
        module.save(tag);
        return tag;
    }
}
