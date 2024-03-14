package com.teamresourceful.resourcefullib.common.menu;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import org.jetbrains.annotations.ApiStatus;

public interface ContentMenuProvider<C extends MenuContent<C>> extends MenuProvider {

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.21")
    default C createContent() {
        throw new UnsupportedOperationException("createContent");
    }

    default C createContent(ServerPlayer player) {
        return createContent();
    }

    default void openMenu(ServerPlayer player) {
        MenuContentHelper.open(player, this);
    }

    default boolean resetMouseOnOpen() {
        return true;
    }
}
