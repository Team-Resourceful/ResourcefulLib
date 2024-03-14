package com.teamresourceful.resourcefullib.common.menu.neoforge;

import com.teamresourceful.resourcefullib.common.menu.ContentMenuProvider;
import com.teamresourceful.resourcefullib.common.menu.MenuContent;
import com.teamresourceful.resourcefullib.common.menu.MenuContentHelper;
import com.teamresourceful.resourcefullib.common.menu.MenuContentSerializer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MenuContentHelperImpl {
    public static <T extends AbstractContainerMenu, C extends MenuContent<C>> MenuType<T> create(MenuContentHelper.MenuFactory<T, C> factory, MenuContentSerializer<C> serializer) {
        return IMenuTypeExtension.create((id, inventory, data) -> {
            if (serializer != null) {
                return factory.create(id, inventory, Optional.ofNullable(serializer.from(data)));
            }
            return factory.create(id, inventory, Optional.empty());
        });
    }

    public static <C extends MenuContent<C>> void open(ServerPlayer player, ContentMenuProvider<C> provider) {
        player.openMenu(new ScreenFactory<>(provider), buf -> {
            C content = provider.createContent(player);
            if (content != null) {
                content.serializer().to(buf, content);
            }
        });
    }

    private record ScreenFactory<C extends MenuContent<C>>(ContentMenuProvider<C> provider) implements MenuProvider {
        @Override
        public @NotNull Component getDisplayName() {
            return provider.getDisplayName();
        }

        @Nullable
        @Override
        public AbstractContainerMenu createMenu(int i, @NotNull Inventory arg, @NotNull Player arg2) {
            return provider.createMenu(i, arg, arg2);
        }

        @Override
        public boolean shouldTriggerClientSideContainerClosingOnOpen() {
            return provider.resetMouseOnOpen();
        }
    }
}
