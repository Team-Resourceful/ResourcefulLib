package com.teamresourceful.resourcefullib.common.utils.modinfo;

import java.nio.file.Path;
import java.util.List;

public interface ModInfo {

    String displayName();

    String id();

    String version();

    /**
     * Returns a list of paths that the mod is loaded from.
     * @return A list of paths that the mod is loaded from. The list is immutable.
     */
    List<Path> getPaths();
}
