package com.teamresourceful.resourcefullib.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public interface ScreenHistory {

    /**
     * Sets the last screen that was displayed.
     * Should throw an exception if the screen is null or screen is already set.
     */
    void setLastScreen(Screen screen);

    /**
     * Returns the screen that needs to be displayed, if null is returned no screen will be changed
     * null will be returned if sub screens have an old screen that needs to be displayed.
     */
    @Nullable
    Screen getLastScreen();

    /**
     * Has the ability to change back a screen.
     */
    boolean canGoBack();

    /**
     * Goes back a screen.
     */
    default void goBack() {
        if (this.canGoBack()) {
            Screen lastScreen = this.getLastScreen();
            if (lastScreen != null) {
                Minecraft.getInstance().setScreen(lastScreen);
            }
        }
    }
}
