package com.teamresourceful.resourcefullib.common.menu.fabric;

import com.teamresourceful.resourcefullib.common.menu.ContentMenuProvider;
import com.teamresourceful.resourcefullib.common.menu.MenuContent;
import com.teamresourceful.resourcefullib.common.menu.MenuContentHelper;
import com.teamresourceful.resourcefullib.common.menu.MenuContentSerializer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MenuContentHelperImpl {
    public static <T extends AbstractContainerMenu, C extends MenuContent<C>> MenuType<T> create(MenuContentHelper.MenuFactory<T, C> factory, MenuContentSerializer<C> serializer) {
        return new ExtendedScreenHandlerType<>((id, inventory, data) -> {
            if (serializer != null) {
                return factory.create(id, inventory, Optional.ofNullable(serializer.from(data)));
            }
            return factory.create(id, inventory, Optional.empty());
        });
    }

    public static <C extends MenuContent<C>> void open(ServerPlayer player, ContentMenuProvider<C> provider) {
        player.openMenu(new ScreenFactory<>(provider));
    }

    private record ScreenFactory<C extends MenuContent<C>>(ContentMenuProvider<C> provider) implements ExtendedScreenHandlerFactory {

        @Override
        public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
            C content = provider.createContent(player);
            if (content != null) {
                content.serializer().to(buf, content);
            }
        }

        @Override
        public @NotNull Component getDisplayName() {
            return provider.getDisplayName();
        }

        @Nullable
        @Override
        public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
            return provider.createMenu(i, inventory, player);
        }
    }

}
