package com.teamresourceful.resourcefullib.client.highlights;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.teamresourceful.resourcefullib.client.highlights.base.Highlight;
import com.teamresourceful.resourcefullib.client.highlights.base.Highlightable;
import com.teamresourceful.resourcefullib.client.highlights.state.HighlightStates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class HighlightHandler extends SimpleJsonResourceReloadListener {

    private static final Map<BlockState, Highlight> STATE_CACHE = new HashMap<>();
    private static final Map<ResourceLocation, Highlight> BOX_CACHE = new HashMap<>();

    public static final Codec<Highlight> HIGHLIGHT_CODEC = ResourceLocation.CODEC.xmap(BOX_CACHE::get, Highlight::id);

    public HighlightHandler() {
        super(new Gson(), "resourcefullib/highlights");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> jsons, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        BOX_CACHE.clear();
        STATE_CACHE.clear();

        Map<ResourceLocation, JsonElement> highlights = new HashMap<>();
        Map<ResourceLocation, JsonElement> blocks = new HashMap<>();
        jsons.forEach((key, json) -> (json.isJsonObject() && json.getAsJsonObject().has("lines") ? highlights : blocks).put(key, json));

        highlights.forEach((key, value) -> Highlight.codec(key).parse(JsonOps.INSTANCE, value).result().ifPresent(box -> BOX_CACHE.put(key, box)));

        blocks.forEach((key, value) -> Registry.BLOCK.getOptional(key)
            .flatMap(block -> HighlightStates.codec(block).parse(JsonOps.INSTANCE, value).result())
            .ifPresent(variants -> variants.states().forEach((states, box) -> states.forEach(state -> STATE_CACHE.put(state, box))))
        );
    }

    public static boolean onBlockHighlight(Vec3 cameraPos, Entity cameraEntity, PoseStack stack, BlockPos blockPos, BlockState state, VertexConsumer consumer) {
        if (state.getBlock() instanceof Highlightable highlightable) {
            var highlight = highlightable.getHighlight(cameraEntity.level, blockPos, state);
            if (highlight != null) {
                highlight.render(consumer, stack, cameraPos, blockPos);
                return true;
            }
        }
        if (STATE_CACHE.containsKey(state)) {
            STATE_CACHE.get(state).render(consumer, stack, cameraPos, blockPos);
            return true;
        }
        return false;
    }
}
