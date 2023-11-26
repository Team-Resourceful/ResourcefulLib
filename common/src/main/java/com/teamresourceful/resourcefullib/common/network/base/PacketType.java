package com.teamresourceful.resourcefullib.common.network.base;

import com.teamresourceful.resourcefullib.common.network.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface PacketType<T extends Packet<T>> {

    Class<T> type();

    ResourceLocation id();

    void encode(T message, FriendlyByteBuf buffer);

    T decode(FriendlyByteBuf buffer);
}
