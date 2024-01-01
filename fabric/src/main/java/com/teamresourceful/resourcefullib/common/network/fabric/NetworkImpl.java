package com.teamresourceful.resourcefullib.common.network.fabric;

import com.teamresourceful.resourcefullib.common.network.base.Networking;
import net.minecraft.resources.ResourceLocation;

public class NetworkImpl {
    public static Networking getNetwork(ResourceLocation channel, int protocolVersion, boolean optional) {
        return new FabricNetworking(channel, protocolVersion);
    }
}
