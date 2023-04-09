package com.teamresourceful.resourcefullib.client.utils;


import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class CursorUtils {

    private static final long DEFAULT_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
    private static final long POINTING_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_POINTING_HAND_CURSOR);
    private static final long DISABLED_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_NOT_ALLOWED_CURSOR);
    private static final long TEXT_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR);
    private static final long CROSSHAIR_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_CROSSHAIR_CURSOR);
    private static final long RESIZE_EW_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_EW_CURSOR);
    private static final long RESIZE_NS_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_NS_CURSOR);
    private static final long RESIZE_NWSE_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_NWSE_CURSOR);
    private static final long RESIZE_NESW_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_NESW_CURSOR);
    private static final long RESIZE_ALL_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_ALL_CURSOR);

    private CursorUtils() throws UtilityClassException {
        throw new UtilityClassException();
    }

    public static void setDefault() {
        GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), CursorUtils.DEFAULT_CURSOR);
    }

    public static void setPointing() {
        GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), CursorUtils.POINTING_CURSOR);
    }

    public static void setDisabled() {
        GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), CursorUtils.DISABLED_CURSOR);
    }

    public static void setText() {
        GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), CursorUtils.TEXT_CURSOR);
    }

    public static void setCrosshair() {
        GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), CursorUtils.CROSSHAIR_CURSOR);
    }

    public static void setResizeEastWest() {
        GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), CursorUtils.RESIZE_EW_CURSOR);
    }

    public static void setResizeNorthSouth() {
        GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), CursorUtils.RESIZE_NS_CURSOR);
    }

    public static void setResizeNorthWestSouthEast() {
        GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), CursorUtils.RESIZE_NWSE_CURSOR);
    }

    public static void setResizeNorthEastSouthWest() {
        GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), CursorUtils.RESIZE_NESW_CURSOR);
    }

    public static void setResizeAll() {
        GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), CursorUtils.RESIZE_ALL_CURSOR);
    }

    public static void setCursor(boolean state, CursorScreen.Cursor cursor) {
        if (Minecraft.getInstance().screen instanceof CursorScreen cursorScreen && state) {
            cursorScreen.setCursor(cursor);
        }
    }

}
