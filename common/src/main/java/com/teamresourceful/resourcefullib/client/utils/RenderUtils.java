package com.teamresourceful.resourcefullib.client.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.scissor.CloseableScissorStack;
import com.teamresourceful.resourcefullib.client.scissor.ClosingScissorBox;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.joml.Vector3f;

public final class RenderUtils {

    private RenderUtils() throws UtilityClassException {
        throw new UtilityClassException();
    }

    /**
     * Get the current bounds for a GL scissor.
     */
    public static Rect2i getScissorRect(Minecraft minecraft, PoseStack stack, int x, int y, int width, int height) {
        float guiScale = (float) minecraft.getWindow().getGuiScale();
        Vector2ic translation = getTranslation(stack);
        float translationX = translation.x() * guiScale;
        float translationY = translation.y() * guiScale;
        return new Rect2i((int) (translationX + x * guiScale), (int) (Minecraft.getInstance().getWindow().getHeight() - y * guiScale - translationY - height * guiScale), (int) (width * guiScale), (int) (height * guiScale));
    }

    /**
     * @return returns the point of the current translation of the stack.
     */
    public static Vector2ic getTranslation(PoseStack stack) {
        Matrix4f pose = stack.last().pose();
        Vector3f vec = pose.getTranslation(new Vector3f());
        return new Vector2i((int) vec.x, (int) vec.y);
    }

    /**
     * Returns a scissor box using the stack's translation and the given bounds.
     * <br>
     * You likely want to use {@link #createScissor(Minecraft, GuiGraphics, int, int, int, int)} instead.
     */
    public static ClosingScissorBox createScissorBox(Minecraft minecraft, PoseStack stack, int x, int y, int width, int height) {
        Rect2i bound = getScissorRect(minecraft, stack, x, y, width, height);
        return new ClosingScissorBox(bound.getX(), bound.getY(), bound.getWidth(), bound.getHeight());
    }

    /**
     * Returns a scissor box stack using the stack's translation and the given bounds.
     * <br>
     * You likely want to use {@link #createScissor(Minecraft, GuiGraphics, int, int, int, int)} instead.
     */
    public static CloseableScissorStack createScissorBoxStack(ScissorBoxStack scissorStack, Minecraft minecraft, PoseStack stack, int x, int y, int width, int height) {
        Rect2i bound = getScissorRect(minecraft, stack, x, y, width, height);
        return new CloseableScissorStack(scissorStack, bound.getX(), bound.getY(), bound.getWidth(), bound.getHeight());
    }

    /**
     * Returns a scissor box stack using the stack's translation and the given bounds.
     */
    public static CloseableScissorStack createScissor(Minecraft minecraft, GuiGraphics graphics, int x, int y, int width, int height) {
        Rect2i bound = getScissorRect(minecraft, graphics.pose(), x, y, width, height);
        return new CloseableScissorStack(new ScissorBoxStack(), bound.getX(), bound.getY(), bound.getWidth(), bound.getHeight());
    }
}