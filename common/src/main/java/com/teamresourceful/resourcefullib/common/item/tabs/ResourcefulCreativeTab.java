package com.teamresourceful.resourcefullib.common.item.tabs;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ResourcefulCreativeTab {

    public final ResourceLocation id;
    public Supplier<ItemStack> icon;
    public boolean hideScrollBar;
    public boolean hideTitle;

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.21")
    public final List<ResourcefulRegistry<ItemLike>> registries = new ArrayList<>();
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.21")
    public final List<Supplier<ItemStack>> stacks = new ArrayList<>();

    public final List<Supplier<Stream<ItemStack>>> contents = new ArrayList<>();

    public ResourcefulCreativeTab(ResourceLocation id) {
        this.id = id;
    }

    public ResourcefulCreativeTab setItemIcon(Supplier<? extends ItemLike> icon) {
        return setStackIcon(() -> new ItemStack(icon.get()));
    }

    public ResourcefulCreativeTab setStackIcon(Supplier<ItemStack> icon) {
        this.icon = icon;
        return this;
    }

    public ResourcefulCreativeTab hideTitle() {
        this.hideTitle = true;
        return this;
    }

    public ResourcefulCreativeTab hideScrollBar() {
        this.hideScrollBar = true;
        return this;
    }

    public <I extends ItemLike, T extends ResourcefulRegistry<I>> ResourcefulCreativeTab addRegistry(T registry) {
        return addContent(() -> registry.boundStream().map(ItemStack::new));
    }

    public ResourcefulCreativeTab addStack(Supplier<ItemStack> stack) {
        return addContent(() -> Stream.of(stack.get()));
    }

    public ResourcefulCreativeTab addStack(ItemStack stack) {
        return addStack(() -> stack);
    }

    public ResourcefulCreativeTab addStack(ItemLike item) {
        return addStack(new ItemStack(item));
    }

    public ResourcefulCreativeTab addContent(Supplier<Stream<ItemStack>> content) {
        this.contents.add(content);
        return this;
    }

    public Supplier<CreativeModeTab> build() {
        return create(this);
    }

    @ExpectPlatform
    private static Supplier<CreativeModeTab> create(ResourcefulCreativeTab tab) {
        throw new NotImplementedException();
    }


}
