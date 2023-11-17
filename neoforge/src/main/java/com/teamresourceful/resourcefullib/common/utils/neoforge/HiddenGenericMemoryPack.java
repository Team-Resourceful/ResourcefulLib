package com.teamresourceful.resourcefullib.common.utils.neoforge;

import com.google.gson.JsonObject;
import com.teamresourceful.resourcefullib.common.utils.GenericMemoryPack;
import net.minecraft.server.packs.PackType;

public class HiddenGenericMemoryPack extends GenericMemoryPack {
    protected HiddenGenericMemoryPack(PackType type, String id, JsonObject meta) {
        super(type, id, meta);
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}
