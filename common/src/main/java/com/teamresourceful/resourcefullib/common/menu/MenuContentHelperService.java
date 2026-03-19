package com.teamresourceful.resourcefullib.common.menu;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import com.teamresourceful.resourcefullib.common.lib.PlatformService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@PlatformService
interface MenuContentHelperService {

    <T extends AbstractContainerMenu, C extends MenuContent<C>> MenuType<T> create(MenuContentHelper.MenuFactory<T, C> factory, MenuContentSerializer<C> serializer);
    <C extends MenuContent<C>> void open(ServerPlayer player, ContentMenuProvider<C> provider);

    static MenuContentHelperService create() {
        throw new NotImplementedException();
    }
}
