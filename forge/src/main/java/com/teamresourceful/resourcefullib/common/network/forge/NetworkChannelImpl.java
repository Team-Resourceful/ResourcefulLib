package com.teamresourceful.resourcefullib.common.network.forge;

import com.teamresourceful.resourcefullib.common.network.base.Networking;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BooleanSupplier;

public class NetworkChannelImpl {
    public static Networking getNetwork(ResourceLocation channel, int protocolVersion, BooleanSupplier optional) {
        return new ForgeNetworking(channel, protocolVersion, optional);
    }
}
