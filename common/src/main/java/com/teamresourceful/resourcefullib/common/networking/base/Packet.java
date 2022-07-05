package com.teamresourceful.resourcefullib.common.networking.base;

import net.minecraft.resources.ResourceLocation;

public interface Packet<T extends Packet<T>> {
    ResourceLocation getID();
    PacketHandler<T> getHandler();
}
