package com.teamresourceful.resourcefullib.common.utils.files;

import com.teamresourceful.resourcefullib.common.lib.Constants;
import net.minecraft.Optionull;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.system.Platform;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Creates and manages the cache and data directories for mods.
 * <p>
 * You may use the getCacheDirectory and getDataDirectory methods to get the directories for your mod.
 * <p>
 * This does not handle any of the file IO for you, it only provides the directories paths.
 * You must handle the file IO and creation of the directories yourself.
 */
public class GlobalStorage {

    private static final String ID = "resourcefullib";
    private static final String README = """
            This is created by ResourcefulLib for usage by mods.
            This directory is used to store data/caches for mods.
            """;

    private static boolean initialized = false;

    private static final Path cache;
    private static final Path data;

    static {
        switch (Platform.get()) {
            case WINDOWS -> {
                cache = Path.of(System.getenv("LOCALAPPDATA"), "." + ID, "cache");
                data = Path.of(System.getenv("LOCALAPPDATA"), "." + ID, "data");
            }
            case MACOSX -> {
                cache = Path.of(System.getProperty("user.home"), "Library", "Caches", ID);
                data = Path.of(System.getProperty("user.home"), "Library", "Application Support", ID);
            }
            default -> {
                cache = Optionull.mapOrElse(
                        System.getenv("XDG_CACHE_HOME"),
                        home -> Path.of(home, ID),
                        () -> Path.of(System.getProperty("user.home"), ".cache", ID)
                );
                data = Optionull.mapOrElse(
                        System.getenv("XDG_DATA_HOME"),
                        home -> Path.of(home, ID),
                        () -> Path.of(System.getProperty("user.home"), ".local", "share", ID)
                );
            }
        }
    }

    @ApiStatus.Internal
    public static void init() {
        if (initialized) return;
        initialized = true;
        try {
            Files.createDirectories(cache);
            Files.createDirectories(data);

            Path readme = cache.resolve("README.txt");
            if (!Files.exists(readme)) {
                Files.writeString(readme, README);
            }

            readme = data.resolve("README.txt");
            if (!Files.exists(readme)) {
                Files.writeString(readme, README);
            }
        } catch (Exception e) {
            Constants.LOGGER.warn("Failed to create cache/data directories!", e);
        }
    }

    /**
     * Returns the cache directory for the mod.
     * @param modid The modid of the mod.
     * @return The cache directory for the mod.
     */
    public static Path getCacheDirectory(String modid) {
        init();
        return cache.resolve(modid);
    }

    /**
     * Returns the data directory for the mod.
     * @param modid The modid of the mod.
     * @return The data directory for the mod.
     */
    public static Path getDataDirectory(String modid) {
        init();
        return data.resolve(modid);
    }
}
