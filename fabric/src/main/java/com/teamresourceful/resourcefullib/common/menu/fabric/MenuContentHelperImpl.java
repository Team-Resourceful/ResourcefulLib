package com.teamresourceful.resourcefullib.common.menu.fabric;

import com.teamresourceful.resourcefullib.common.menu.ContentMenuProvider;
import com.teamresourceful.resourcefullib.common.menu.MenuContent;
import com.teamresourceful.resourcefullib.common.menu.MenuContentHelper;
import com.teamresourceful.resourcefullib.common.menu.MenuContentSerializer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MenuContentHelperImpl {
    public static <T extends AbstractContainerMenu, C extends MenuContent<C>> MenuType<T> create(MenuContentHelper.MenuFactory<T, C> factory, @Nullable MenuContentSerializer<C> serializer) {
        StreamCodec<RegistryFriendlyByteBuf, Optional<C>> streamCodec = serializer == null ?
                StreamCodec.unit(Optional.empty()) :
                StreamCodec.of(
                    (buf, opt) -> opt.ifPresent(content -> serializer.to(buf, content)),
                    buf -> Optional.ofNullable(serializer.from(buf))
                );

        return new ExtendedScreenHandlerType<>((id, inventory, data) -> {
            if (serializer != null) {
                return factory.create(id, inventory, data);
            }
            return factory.create(id, inventory, data);
        }, streamCodec);
    }

    public static <C extends MenuContent<C>> void open(ServerPlayer player, ContentMenuProvider<C> provider) {
        player.openMenu(new ScreenFactory<>(provider));
    }

    private record ScreenFactory<C extends MenuContent<C>>(ContentMenuProvider<C> provider) implements ExtendedScreenHandlerFactory<Optional<C>> {

        @Override
        public @NotNull Component getDisplayName() {
            return provider.getDisplayName();
        }

        @Nullable
        @Override
        public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
            return provider.createMenu(i, inventory, player);
        }

        @Override
        public boolean shouldCloseCurrentScreen() {
            return provider.resetMouseOnOpen();
        }

        @Override
        public Optional<C> getScreenOpeningData(ServerPlayer player) {
            return Optional.ofNullable(provider.createContent(player));
        }
    }

}
