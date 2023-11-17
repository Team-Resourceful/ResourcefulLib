package com.teamresourceful.resourcefullib.common.item.tabs;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ResourcefulCreativeModeTab {

    public final ResourceLocation id;
    public Supplier<ItemStack> icon;
    public boolean hideScrollBar;
    public boolean hideTitle;

    public final List<Supplier<Stream<ItemStack>>> contents = new ArrayList<>();

    public ResourcefulCreativeModeTab(ResourceLocation id) {
        this.id = id;
    }

    public ResourcefulCreativeModeTab setItemIcon(Supplier<? extends ItemLike> icon) {
        return setStackIcon(() -> new ItemStack(icon.get()));
    }

    public ResourcefulCreativeModeTab setStackIcon(Supplier<ItemStack> icon) {
        this.icon = icon;
        return this;
    }

    public ResourcefulCreativeModeTab hideTitle() {
        this.hideTitle = true;
        return this;
    }

    public ResourcefulCreativeModeTab hideScrollBar() {
        this.hideScrollBar = true;
        return this;
    }

    public <I extends ItemLike, T extends ResourcefulRegistry<I>> ResourcefulCreativeModeTab addRegistry(T registry) {
        return addContent(() -> registry.boundStream().map(ItemStack::new));
    }

    public ResourcefulCreativeModeTab addStack(Supplier<ItemStack> stack) {
        return addContent(() -> Stream.of(stack.get()));
    }

    public ResourcefulCreativeModeTab addStack(ItemStack stack) {
        return addStack(() -> stack);
    }

    public ResourcefulCreativeModeTab addStack(ItemLike item) {
        return addStack(new ItemStack(item));
    }

    public ResourcefulCreativeModeTab addContent(Supplier<Stream<ItemStack>> content) {
        this.contents.add(content);
        return this;
    }

    public CreativeModeTab build() {
        return create(this);
    }

    @ExpectPlatform
    private static CreativeModeTab create(ResourcefulCreativeModeTab tab) {
        throw new NotImplementedException();
    }


}
