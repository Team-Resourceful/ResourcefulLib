package com.teamresourceful.resourcefullib.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;

public class CloseablePoseStack implements AutoCloseable {

    private final PoseStack stack;

    public CloseablePoseStack(PoseStack stack) {
        this.stack = stack;
        this.stack.pushPose();
    }

    public void translate(double d, double e, double f) {
        stack.translate(d, e, f);
    }

    public void scale(float f, float g, float h) {
        stack.scale(f, g, h);
    }

    public void mulPose(Quaternion quaternion) {
        stack.mulPose(quaternion);
    }

    public PoseStack.Pose last() {
        return stack.last();
    }

    public boolean clear() {
        return stack.clear();
    }

    public void setIdentity() {
        stack.setIdentity();
    }

    public void mulPoseMatrix(Matrix4f matrix4f) {
        stack.mulPoseMatrix(matrix4f);
    }

    @Override
    public void close() {
        stack.popPose();
    }
}
