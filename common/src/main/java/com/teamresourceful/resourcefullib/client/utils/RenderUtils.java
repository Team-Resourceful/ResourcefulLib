package com.teamresourceful.resourcefullib.client.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.scissor.CloseableScissorStack;
import com.teamresourceful.resourcefullib.client.scissor.ClosingScissorBox;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import com.teamresourceful.resourcefullib.common.utils.types.Bound;
import com.teamresourceful.resourcefullib.common.utils.types.Vec2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class RenderUtils {

    private RenderUtils() throws UtilityClassException {
        throw new UtilityClassException();
    }

    /**
     * Binds the given texture to the current render context.
     * @param location the texture
     */
    public static void bindTexture(ResourceLocation location) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, location);
    }

    /**
     * Get the current bounds for a GL scissor.
     */
    public static Bound getScissorBounds(Minecraft minecraft, PoseStack stack, int x, int y, int width, int height) {
        float guiScale = (float) minecraft.getWindow().getGuiScale();
        Vec2i translation = getTranslation(stack);
        float translationX = translation.x() * guiScale;
        float translationY = translation.y() * guiScale;
        return new Bound((int) (translationX + x * guiScale), (int) (Minecraft.getInstance().getWindow().getHeight() - y * guiScale - translationY - height * guiScale), (int) (width * guiScale), (int) (height * guiScale));
    }

    /**
     * @return returns the point of the current translation of the stack.
     */
    public static Vec2i getTranslation(PoseStack stack) {
        Matrix4f pose = stack.last().pose();
        Vector3f vec = pose.getTranslation(new Vector3f());
        return Vec2i.of((int) vec.x, (int) vec.y);
    }

    /**
     * Renders a given item at a position using the translation of a given stack.
     */
    public static void renderItem(PoseStack stack, ItemStack item, int x, int y) {
        Vec2i translation = getTranslation(stack);
        Minecraft.getInstance().getItemRenderer().renderGuiItem(item, translation.x() + x, translation.y() + y);
    }

    /**
     * Returns a scissor box using the stack's translation and the given bounds.
     */
    public static ClosingScissorBox createScissorBox(Minecraft minecraft, PoseStack stack, int x, int y, int width, int height) {
        Bound bound = getScissorBounds(minecraft, stack, x, y, width, height);
        return new ClosingScissorBox(bound.x(), bound.y(), bound.width(), bound.height());
    }

    /**
     * Returns a scissor box stack using the stack's translation and the given bounds.
     */
    public static CloseableScissorStack createScissorBoxStack(ScissorBoxStack scissorStack, Minecraft minecraft, PoseStack stack, int x, int y, int width, int height) {
        Bound bound = getScissorBounds(minecraft, stack, x, y, width, height);
        return new CloseableScissorStack(scissorStack, bound.x(), bound.y(), bound.width(), bound.height());
    }
}
