package com.teamresourceful.resourcefullib.common.utils.neoforge;

import com.teamresourceful.resourcefullib.common.utils.GenericMemoryPack;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;

public class HiddenGenericMemoryPack extends GenericMemoryPack {

    protected HiddenGenericMemoryPack(PackType type, String id, PackMetadataSection meta) {
        super(type, id, meta);
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}
