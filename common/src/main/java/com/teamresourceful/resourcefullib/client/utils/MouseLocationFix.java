package com.teamresourceful.resourcefullib.client.utils;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

import java.util.function.Predicate;

/**
 * This class copies the mouse position and sets it back when fix is called if it's been less than a second since the last call.
 * This is used to fix the mouse position when a menu is opened and closed.
 */
public record MouseLocationFix(double x, double y, Predicate<Class<?>> checker, long time) {

    private static MouseLocationFix fix = null;

    public static void fix(Class<?> screen) {
        if (fix != null && fix.checker.test(screen) && System.currentTimeMillis() - fix.time() < 1000) {
            GLFW.glfwSetCursorPos(Minecraft.getInstance().getWindow().getWindow(), fix.x(), fix.y());
            fix = null;
        }
    }

    public static void setFix(Predicate<Class<?>> checker) {
        double x = Minecraft.getInstance().mouseHandler.xpos();
        double y = Minecraft.getInstance().mouseHandler.ypos();
        fix = new MouseLocationFix(x, y, checker, System.currentTimeMillis());
    }
}
