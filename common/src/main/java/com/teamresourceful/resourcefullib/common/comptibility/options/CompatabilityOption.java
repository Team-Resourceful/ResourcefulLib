package com.teamresourceful.resourcefullib.common.comptibility.options;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;

import java.util.List;

public interface CompatabilityOption<T extends CompatabilityOption<T>> {

    Codec<T> codec();

    String id();

    Component title();

    List<Component> description();

    String url();

    boolean matches();
}
