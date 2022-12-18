package com.teamresourceful.resourcefullib.common.item;

import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public final class OptionalItemStack {

    private OptionalItemStack() throws UtilityClassException {
        throw new UtilityClassException();
    }

    public static Optional<ItemStack> of(ItemStack stack) {
        return Objects.requireNonNull(stack).isEmpty() ? Optional.empty() : Optional.of(stack);
    }

    public static Optional<ItemStack> ofNullable(@Nullable ItemStack stack) {
        return stack == null || stack.isEmpty() ? Optional.empty() : Optional.of(stack);
    }

    public static Optional<ItemStack> empty() {
        return Optional.empty();
    }

}
