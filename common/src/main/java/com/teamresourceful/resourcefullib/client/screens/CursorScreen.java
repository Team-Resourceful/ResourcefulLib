package com.teamresourceful.resourcefullib.client.screens;

public interface CursorScreen {

    void setCursor(Cursor cursor);

    enum Cursor {
        DEFAULT,
        POINTER,
        DISABLED,
        TEXT,
    }
}
