package com.teamresourceful.resourcefullib.common.network.base;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.internal.NetworkPacketPayload;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.NonExtendable
public interface PacketType<T extends Packet<T>> {

    Identifier id();

    void encode(T message, RegistryFriendlyByteBuf buffer);

    T decode(RegistryFriendlyByteBuf buffer);

    @ApiStatus.Internal
    default StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull NetworkPacketPayload<T>> codec(CustomPacketPayload.Type<@NotNull NetworkPacketPayload<T>> type) {
        return StreamCodec.of(
                (buf, payload) -> encode(payload.packet(), buf),
                (buf) -> new NetworkPacketPayload<>(decode(buf), type)
        );
    }

    @ApiStatus.Internal
    default CustomPacketPayload.Type<@NotNull NetworkPacketPayload<T>> type(Identifier channel) {
        return new CustomPacketPayload.Type<>(channel.withSuffix("/" + this.id().getNamespace() + "/" + this.id().getPath()));
    }
}
