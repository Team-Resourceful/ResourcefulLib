package com.teamresourceful.resourcefullib;

import com.teamresourceful.resourcefullib.common.modules.*;
import com.teamresourceful.resourcefullib.common.modules.Module;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.nbt.CompoundTag;

public class ResourcefulLib {
    public static final String MOD_ID = "resourcefullib";

    public static final ResourcefulRegistry<ModuleType<?>> MODULES = ModuleRegistries.create(MOD_ID);
    public static final RegistryEntry<ModuleType<Value>> VALUE = MODULES.register("value",
            () -> SimpleModuleType.builder(Value.class, Value::new)
                    .targets(ModuleTarget.PLAYER)
                    .copy()
                    .build()
    );

    public static void init() {
        MODULES.init();
    }

    public static class Value implements Module {

        public int value = 0;

        public Value(int value) {
            this.value = value;
        }

        public Value() {
            this(0);
        }

        @Override
        public void read(CompoundTag tag) {
            this.value = tag.getInt("value");
        }

        @Override
        public void save(CompoundTag tag) {
            tag.putInt("value", this.value);
        }
    }
}
