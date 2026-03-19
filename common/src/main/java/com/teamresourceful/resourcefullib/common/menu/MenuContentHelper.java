package com.teamresourceful.resourcefullib.common.menu;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class MenuContentHelper {

    private static final MenuContentHelperService SERVICE = MenuContentHelperService.create();

    public static <T extends AbstractContainerMenu, C extends MenuContent<C>> MenuType<T> create(MenuFactory<T, C> factory, MenuContentSerializer<C> serializer) {
        return SERVICE.create(factory, serializer);
    }

    public static <C extends MenuContent<C>> void open(ServerPlayer player, ContentMenuProvider<C> provider) {
        SERVICE.open(player, provider);
    }

    public interface MenuFactory<T extends AbstractContainerMenu, C extends MenuContent<C>> {
        @NotNull T create(int id, Inventory inventory, Optional<C> content);

        default @NotNull T create(int id, @NotNull Inventory inventory) {
            return create(id, inventory, Optional.empty());
        }
    }
}
