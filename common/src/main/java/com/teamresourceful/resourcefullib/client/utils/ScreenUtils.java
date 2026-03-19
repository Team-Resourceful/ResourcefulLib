package com.teamresourceful.resourcefullib.client.utils;

import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import net.minecraft.client.Minecraft;

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
}