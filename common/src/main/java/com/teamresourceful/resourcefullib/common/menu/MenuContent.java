package com.teamresourceful.resourcefullib.common.menu;

public interface MenuContent<T extends MenuContent<T>> {

    MenuContentSerializer<T> serializer();
}
