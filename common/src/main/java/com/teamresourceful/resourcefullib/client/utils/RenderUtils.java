package com.teamresourceful.resourcefullib.client.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rect2i;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector2ic;

@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = ">1.21.6")
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
        return new Vector2i((int) pose.m30(), (int) pose.m31());
    }
}