package com.teamresourceful.resourcefullib.client.screens.state;

import net.minecraft.client.gui.screens.Screen;

public interface PageState<T> {

    Screen createScreen(T data);
}
