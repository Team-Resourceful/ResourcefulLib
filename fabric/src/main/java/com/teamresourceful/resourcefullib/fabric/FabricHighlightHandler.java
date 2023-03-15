package com.teamresourceful.resourcefullib.fabric;

import com.teamresourceful.resourcefullib.ResourcefulLib;
import com.teamresourceful.resourcefullib.client.highlights.HighlightHandler;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class FabricHighlightHandler extends HighlightHandler implements IdentifiableResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
        return new ResourceLocation(ResourcefulLib.MOD_ID, "highlights");
    }
}
