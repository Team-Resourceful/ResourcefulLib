package com.teamresourceful.resourcefullib.client.highlights;

import com.google.gson.JsonElement;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.teamresourceful.resourcefullib.client.highlights.base.Highlight;
import com.teamresourceful.resourcefullib.client.highlights.base.HighlightLine;
import com.teamresourceful.resourcefullib.client.highlights.base.Highlightable;
import com.teamresourceful.resourcefullib.client.highlights.state.HighlightStates;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class HighlightHandler extends SimpleJsonResourceReloadListener<@NotNull JsonElement> {

    private static final Reference2ReferenceMap<BlockState, float[]> STATE_CACHE = new Reference2ReferenceOpenHashMap<>();
    private static final Map<Identifier, Highlight> BOX_CACHE = new HashMap<>();

    public static final Codec<Highlight> HIGHLIGHT_CODEC = Identifier.CODEC.xmap(HighlightHandler::getOrThrow, Highlight::id);

    public HighlightHandler() {
        super(ExtraCodecs.JSON, FileToIdConverter.json("resourcefullib/highlights"));
    }

    @Override
    protected void apply(@NotNull Map<Identifier, JsonElement> jsons, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        BOX_CACHE.clear();
        STATE_CACHE.clear();

        Map<Identifier, JsonElement> highlights = new HashMap<>();
        Map<Identifier, JsonElement> blocks = new HashMap<>();
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

    public static @Nullable HighlightRenderState extractState(Level level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof Highlightable highlightable) {
            var highlight = highlightable.getHighlight(level, pos, state);
            if (highlight != null) {
                return new HighlightRenderState.Dynamic(highlight, state.getOffset(pos));
            }
        }
        if (STATE_CACHE.containsKey(state)) {
            return new HighlightRenderState.Cached(STATE_CACHE.get(state), state.getOffset(pos));
        }
        return null;
    }

    public static boolean canRender(HighlightRenderState state) {
        return state instanceof HighlightRenderState.Dynamic || (state instanceof HighlightRenderState.Cached(var lines, var offset) && lines.length % 9 == 0);
    }

    @Deprecated(forRemoval = true)
    public static boolean onBlockHighlight(Vec3 cameraPos, PoseStack stack, BlockPos pos, HighlightRenderState state, VertexConsumer consumer, int color) {
        return onBlockHighlight(cameraPos, stack, pos, state, consumer, color, Minecraft.getInstance().getWindow().getAppropriateLineWidth());
    }

    public static boolean onBlockHighlight(Vec3 cameraPos, PoseStack stack, BlockPos pos, HighlightRenderState state, VertexConsumer consumer, int color, float width) {
        if (state instanceof HighlightRenderState.Dynamic(var highlight, var offset)) {
            highlight.render(consumer, stack, cameraPos, offset, pos);
            return true;
        } else if (state instanceof HighlightRenderState.Cached(var lines, var offset) && lines.length % 9 == 0) {
            stack.pushPose();
            float x = (float) (pos.getX() - cameraPos.x());
            float y = (float) (pos.getY() - cameraPos.y());
            float z = (float) (pos.getZ() - cameraPos.z());
            x += (float) offset.x();
            y += (float) offset.y();
            z += (float) offset.z();

            for (int i = 0; i < lines.length; i += 9) {
                HighlightLine.render(
                        stack, consumer,
                        color, width,
                        x, y, z,
                        lines[i], lines[i + 1], lines[i + 2],
                        lines[i + 3], lines[i + 4], lines[i + 5],
                        lines[i + 6], lines[i + 7], lines[i + 8]
                );
            }
            stack.popPose();

            return true;
        }
        return false;
    }

    private static Highlight getOrThrow(Identifier id) {
        var highlight = BOX_CACHE.get(id);
        if (highlight == null) throw new RuntimeException("No highlight with the id '" + id + "' was found!");
        return highlight;
    }
}
