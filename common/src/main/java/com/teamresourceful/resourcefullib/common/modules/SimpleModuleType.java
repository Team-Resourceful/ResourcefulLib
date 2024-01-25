package com.teamresourceful.resourcefullib.common.modules;

import net.minecraft.resources.ResourceLocation;

import java.util.EnumSet;
import java.util.function.Supplier;

public record SimpleModuleType<T extends Module<T>>(
        ResourceLocation id,
        Class<T> type,
        Supplier<T> factory,
        boolean copy,
        EnumSet<ModuleTarget> targets
) implements ModuleType<T> {

    public static <T extends Module<T>> Builder<T> builder(ResourceLocation id, Class<T> type, Supplier<T> factory) {
        return new Builder<>(id, type, factory);
    }

    @Override
    public T create() {
        return factory.get();
    }

    public static class Builder<T extends Module<T>> {

        private final ResourceLocation id;
        private final Class<T> type;
        private final Supplier<T> factory;

        private boolean copy = false;
        private EnumSet<ModuleTarget> targets = EnumSet.noneOf(ModuleTarget.class);

        protected Builder(ResourceLocation id, Class<T> type, Supplier<T> factory) {
            this.id = id;
            this.type = type;
            this.factory = factory;
        }

        public Builder<T> copy() {
            this.copy = true;
            return this;
        }

        public Builder<T> targets(ModuleTarget... targets) {
            this.targets = EnumSet.of(targets[0], targets);
            return this;
        }

        public SimpleModuleType<T> build() {
            return new SimpleModuleType<>(id, type, factory, copy, targets);
        }
    }
}
