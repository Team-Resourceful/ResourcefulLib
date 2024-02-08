package com.teamresourceful.resourcefullib.common.modules;

import java.util.EnumSet;
import java.util.function.Supplier;

public record SimpleModuleType<T extends Module>(
        Class<T> type,
        Supplier<T> factory,
        boolean copy,
        EnumSet<ModuleTarget> targets
) implements ModuleType<T> {

    public static <T extends Module> Builder<T> builder(Class<T> type, Supplier<T> factory) {
        return new Builder<>(type, factory);
    }

    @Override
    public T create() {
        return factory.get();
    }

    public static class Builder<T extends Module> {

        private final Class<T> type;
        private final Supplier<T> factory;

        private boolean copy = false;
        private EnumSet<ModuleTarget> targets = EnumSet.noneOf(ModuleTarget.class);

        protected Builder(Class<T> type, Supplier<T> factory) {
            this.type = type;
            this.factory = factory;
        }

        public Builder<T> copy(boolean copy) {
            this.copy = copy;
            return this;
        }

        public Builder<T> copy() {
            return copy(true);
        }

        public Builder<T> targets(ModuleTarget... targets) {
            this.targets = EnumSet.of(targets[0], targets);
            return this;
        }

        public SimpleModuleType<T> build() {
            return new SimpleModuleType<>(type, factory, copy, targets);
        }
    }
}
