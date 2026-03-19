package com.teamresourceful.resourcefullib.common.network;

import com.teamresourceful.resourcefullib.common.network.base.Networking;
import net.minecraft.resources.Identifier;

class NetworkServiceFabricImpl implements NetworkService {
    @Override
    public Networking getNetwork(Identifier channel, int protocolVersion, boolean optional) {
        return new FabricNetworking(channel, protocolVersion);
    }
}
