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

public class ResourcefulCreativeTab {

    public final ResourceLocation id;
    public Supplier<ItemStack> icon;
    public boolean hideScrollBar;
    public boolean hideTitle;

    public final List<ResourcefulRegistry<ItemLike>> registries = new ArrayList<>();
    public final List<Supplier<ItemStack>> stacks = new ArrayList<>();

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

    @SuppressWarnings("unchecked")
    public <I extends ItemLike, T extends ResourcefulRegistry<I>> ResourcefulCreativeTab addRegistry(T registry) {
        this.registries.add((ResourcefulRegistry<ItemLike>) registry);
        return this;
    }

    public ResourcefulCreativeTab addStack(Supplier<ItemStack> stack) {
        this.stacks.add(stack);
        return this;
    }

    public ResourcefulCreativeTab addStack(ItemStack stack) {
        this.stacks.add(() -> stack);
        return this;
    }

    public ResourcefulCreativeTab addStack(ItemLike item) {
        this.stacks.add(() -> new ItemStack(item));
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
