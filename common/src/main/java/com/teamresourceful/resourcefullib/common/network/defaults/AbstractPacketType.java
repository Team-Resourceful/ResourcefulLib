package com.teamresourceful.resourcefullib.common.network.defaults;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractPacketType<T extends Packet<T>> implements PacketType<T> {

    protected final Class<T> clazz;
    protected final ResourceLocation id;

    public AbstractPacketType(Class<T> clazz, ResourceLocation id) {
        this.clazz = clazz;
        this.id = id;
    }

    @Override
    public ResourceLocation id() {
        return id;
    }

    public static abstract class Client<T extends Packet<T>> extends AbstractPacketType<T> implements ClientboundPacketType<T> {

        public Client(Class<T> clazz, ResourceLocation id) {
            super(clazz, id);
        }
    }

    public static abstract class Server<T extends Packet<T>> extends AbstractPacketType<T> implements ClientboundPacketType<T> {

        public Server(Class<T> clazz, ResourceLocation id) {
            super(clazz, id);
        }
    }
}