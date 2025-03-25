package com.teamresourceful.resourcefullib.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;

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
    public void translate(Vec3 vec3) {
        stack.translate(vec3);
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
    public void mulPose(Quaternionfc quaternionfc) {
        stack.mulPose(quaternionfc);
    }

    @Override
    public void rotateAround(Quaternionfc quaternionfc, float f, float g, float h) {
        stack.rotateAround(quaternionfc, f, g, h);
    }

    @NotNull
    @Override
    public PoseStack.Pose last() {
        return stack.last();
    }

    @Override
    public void setIdentity() {
        stack.setIdentity();
    }

    @Override
    public void mulPose(Matrix4fc matrix4fc) {
        stack.mulPose(matrix4fc);
    }

    @Override
    public void close() {
        stack.popPose();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
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
