package com.teamresourceful.resourcefullib.common.network.defaults;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractPacketType<T extends Packet<T>> implements PacketType<T> {

    protected final Class<T> clazz;
    protected final ResourceLocation id;

    public AbstractPacketType(ResourceLocation id) {
        this(null, id);
    }

    public AbstractPacketType(Class<T> clazz, ResourceLocation id) {
        this.clazz = clazz;
        this.id = id;
    }

    @Override
    public ResourceLocation id() {
        return id;
    }
}
