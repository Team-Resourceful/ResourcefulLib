package com.teamresourceful.resourcefullib.client.utils;

import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.jetbrains.annotations.ApiStatus;

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

    /**
     * @deprecated use {@link ScreenRectangle#containsPoint(int, int)} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = ">1.21.9")
    public static boolean inBounds(ScreenRectangle rectangle, int x, int y) {
        return x >= rectangle.left() && x <= rectangle.right() && y >= rectangle.top() && y <= rectangle.bottom();
    }
}