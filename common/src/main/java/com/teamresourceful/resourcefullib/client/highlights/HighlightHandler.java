package com.teamresourceful.resourcefullib.client.highlights;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import com.teamresourceful.resourcefullib.client.highlights.base.Highlight;
import com.teamresourceful.resourcefullib.client.highlights.base.HighlightLine;
import com.teamresourceful.resourcefullib.client.highlights.base.Highlightable;
import com.teamresourceful.resourcefullib.client.highlights.state.HighlightStates;
import com.teamresourceful.resourcefullib.common.color.Color;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
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

    private static final Reference2ReferenceMap<BlockState, float[]> STATE_CACHE = new Reference2ReferenceOpenHashMap<>();
    private static final Map<ResourceLocation, Highlight> BOX_CACHE = new HashMap<>();

    public static final Codec<Highlight> HIGHLIGHT_CODEC = ResourceLocation.CODEC.xmap(HighlightHandler::getOrThrow, Highlight::id);
    private static float red = 0f;
    private static float green = 0f;
    private static float blue = 0f;
    private static float alpha = 0.4f;

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

        Map<Highlight, float[]> cache = new HashMap<>();

        blocks.forEach((key, value) -> BuiltInRegistries.BLOCK.getOptional(key)
                .flatMap(block -> HighlightStates.codec(block).parse(JsonOps.INSTANCE, value).result())
                .ifPresent(variants -> variants.states().forEach((states, box) -> states.forEach(state -> {
                    if (cache.containsKey(box)) {
                        STATE_CACHE.put(state, cache.get(box));
                    } else {
                        float[] lines = new float[box.lines().size() * 9];
                        int i = 0;
                        for (HighlightLine line : box.lines()) {
                            lines[i++] = line.start().x();
                            lines[i++] = line.start().y();
                            lines[i++] = line.start().z();
                            lines[i++] = line.end().x();
                            lines[i++] = line.end().y();
                            lines[i++] = line.end().z();
                            lines[i++] = line.normal().x();
                            lines[i++] = line.normal().y();
                            lines[i++] = line.normal().z();
                        }
                        STATE_CACHE.put(state, lines);
                        cache.put(box, lines);
                    }
                })))
        );

        BOX_CACHE.clear();
    }

    public static boolean onBlockHighlight(Vec3 cameraPos, Entity cameraEntity, PoseStack stack, BlockPos blockPos, BlockState state, VertexConsumer consumer) {
        if (state.getBlock() instanceof Highlightable highlightable) {
            var highlight = highlightable.getHighlight(cameraEntity.level(), blockPos, state);
            if (highlight != null) {
                highlight.render(consumer, stack, cameraPos, state.getOffset(cameraEntity.level(), blockPos), blockPos);
                return true;
            }
        }
        if (STATE_CACHE.containsKey(state)) {
            Vec3 offset = state.getOffset(cameraEntity.level(), blockPos);
            try (var ignored = new CloseablePoseStack(stack)) {
                float x = (float) (blockPos.getX() - cameraPos.x());
                float y = (float) (blockPos.getY() - cameraPos.y());
                float z = (float) (blockPos.getZ() - cameraPos.z());
                x += (float) offset.x();
                y += (float) offset.y();
                z += (float) offset.z();

                float[] lines = STATE_CACHE.get(state);
                if (lines.length % 9 != 0) return false;

                for (int i = 0; i < lines.length; i += 9) {
                    HighlightLine.render(
                            stack, consumer,
                            red, green, blue, alpha,
                            x, y, z,
                            lines[i], lines[i + 1], lines[i + 2],
                            lines[i + 3], lines[i + 4], lines[i + 5],
                            lines[i + 6], lines[i + 7], lines[i + 8]
                    );
                }
            }

            return true;
        }
        return false;
    }

    private static Highlight getOrThrow(ResourceLocation id) {
        var highlight = BOX_CACHE.get(id);
        if (highlight == null) throw new RuntimeException("No highlight with the id '" + id + "' was found!");
        return highlight;
    }

    public static void setColor(int value) {
        Color color = new Color(value);
        red = color.getFloatRed();
        green = color.getFloatGreen();
        blue = color.getFloatBlue();
        alpha = color.getFloatAlpha();
    }
}
