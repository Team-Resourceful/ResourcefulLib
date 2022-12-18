package com.teamresourceful.resourcefullib.client.highlights.base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

import java.util.List;

public record HighlightLine(Vector3f start, Vector3f end, Vector3f normal) {

        public static final Codec<HighlightLine> CODEC = Codec.FLOAT.listOf().comapFlatMap((floats) -> Util.fixedSize(floats, 6).map(HighlightLine::new), HighlightLine::getAsList);

        public HighlightLine(Vector3f start, Vector3f end) {
            this(start, end, normal(start, end));
        }

        public HighlightLine(List<Float> list) {
            this(new Vector3f(list.get(0), list.get(1), list.get(2)), new Vector3f(list.get(3), list.get(4), list.get(5)));
        }

        public List<Float> getAsList() {
            return List.of(start.x(), start.y(), start.z(), end.x(), end.y(), end.z());
        }

        public HighlightLine copy() {
            return new HighlightLine(new Vector3f(start), new Vector3f(end));
        }

        public void recalculateNormal() {
            Vector3f normal = normal(start, end);
            this.normal.set(normal.x(), normal.y(), normal.z());
        }

        public void render(PoseStack poseStack, VertexConsumer consumer, float x, float y, float z) {
            PoseStack.Pose last = poseStack.last();

            consumer.vertex(last.pose(), x+start.x(), y+start.y(), z+start.z()).color(0f, 0f, 0f, 0.4f).normal(last.normal(), normal.x(), normal.y(), normal.z()).endVertex();
            consumer.vertex(last.pose(), x+end.x(), y+end.y(), z+end.z()).color(0f, 0f, 0f, 0.4f).normal(last.normal(), normal.x(), normal.y(), normal.z()).endVertex();
        }

        private static Vector3f normal(Vector3f start, Vector3f end) {
            Vector3f diff = new Vector3f(start.x() - end.x(), start.y() - end.y(), start.z() - end.z());
            float sqrt = Mth.sqrt(diff.x() * diff.x() + diff.y() * diff.y() + diff.z() * diff.z());
            return new Vector3f(diff.x() / sqrt, diff.y() / sqrt, diff.z() / sqrt);
        }
    }