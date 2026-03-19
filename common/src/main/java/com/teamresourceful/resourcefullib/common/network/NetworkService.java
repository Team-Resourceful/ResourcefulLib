package com.teamresourceful.resourcefullib.common.network;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import com.teamresourceful.resourcefullib.common.lib.PlatformService;
import com.teamresourceful.resourcefullib.common.network.base.Networking;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@PlatformService
interface NetworkService {

    Networking getNetwork(Identifier channel, int protocolVersion, boolean optional);

     static NetworkService create() {
        throw new NotImplementedException();
    }
}
