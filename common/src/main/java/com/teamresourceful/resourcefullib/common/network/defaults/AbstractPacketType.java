package com.teamresourceful.resourcefullib.common.network.defaults;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import net.minecraft.resources.Identifier;

public abstract class AbstractPacketType<T extends Packet<T>> implements PacketType<T> {

    protected final Class<T> clazz;
    protected final Identifier id;

    public AbstractPacketType(Identifier id) {
        this(null, id);
    }

    public AbstractPacketType(Class<T> clazz, Identifier id) {
        this.clazz = clazz;
        this.id = id;
    }

    @Override
    public Identifier id() {
        return id;
    }
}
