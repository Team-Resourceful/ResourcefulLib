package com.teamresourceful.resourcefullib.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class CloseablePoseStack extends PoseStack implements AutoCloseable {

    private final PoseStack stack;

    public CloseablePoseStack(PoseStack stack) {
        super();
        this.stack = stack;
        this.stack.pushPose();
    }

    public CloseablePoseStack(GuiGraphics graphics) {
        this(graphics.pose());
    }

    public CloseablePoseStack() {
        this(new PoseStack());
    }

    @Override
    public void translate(double d, double e, double f) {
        stack.translate(d, e, f);
    }

    @Override
    public void translate(float f, float g, float h) {
        stack.translate(f, g, h);
    }

    @Override
    public void scale(float f, float g, float h) {
        stack.scale(f, g, h);
    }

    @Override
    public void mulPose(@NotNull Quaternionf quaternion) {
        stack.mulPose(quaternion);
    }

    @NotNull
    @Override
    public PoseStack.Pose last() {
        return stack.last();
    }

    @Override
    public boolean clear() {
        return stack.clear();
    }

    @Override
    public void setIdentity() {
        stack.setIdentity();
    }

    @Override
    public void mulPoseMatrix(@NotNull Matrix4f matrix4f) {
        stack.mulPoseMatrix(matrix4f);
    }

    @Override
    public void close() {
        stack.popPose();
    }

    @Override
    public void pushPose() {
        stack.pushPose();
    }

    @Override
    public void popPose() {
        stack.popPose();
    }
}
