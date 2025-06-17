package com.teamresourceful.resourcefullib.client.utils;

import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;

public final class ScreenUtils {

    private ScreenUtils() throws UtilityClassException {
        throw new UtilityClassException();
    }

    public static void sendCommand(String command) {
        if (Minecraft.getInstance().getConnection() != null) {
            Minecraft.getInstance().getConnection().sendUnattendedCommand(command, null);
        }
    }

    public static void sendClick(int containerId, int buttonId) {
        if (Minecraft.getInstance().gameMode != null) {
            Minecraft.getInstance().gameMode.handleInventoryButtonClick(containerId, buttonId);
        }
    }

    public static boolean inBounds(ScreenRectangle rectangle, int x, int y) {
        return x >= rectangle.left() && x <= rectangle.right() && y >= rectangle.top() && y <= rectangle.bottom();
    }
}