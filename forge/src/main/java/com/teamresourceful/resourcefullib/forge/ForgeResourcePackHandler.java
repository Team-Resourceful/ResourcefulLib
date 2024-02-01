package com.teamresourceful.resourcefullib.forge;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.teamresourceful.resourcefullib.common.lib.Constants;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ForgeResourcePackHandler {

    private static final List<ResourcePack> PACKS = new ArrayList<>();
    private static final String RESOURCE_PACK_KEY = "resourcefullib:resourcepack";

    public static void load() {
        for (IModInfo mod : ModList.get().getMods()) {
            if (mod.getModProperties().containsKey(RESOURCE_PACK_KEY)) {
                try {
                    initMod(mod, mod.getModProperties());
                } catch (Exception e) {
                    Constants.LOGGER.error("Resourceful Lib failed to load resource pack for mod: " + mod.getDisplayName(), e);
                }
            }
        }
    }

    private static void initMod(IModInfo mod, Map<String, Object> metadata) {
        for (Object pack : (List<?>) metadata.get(RESOURCE_PACK_KEY)) {
            loadPack(mod, pack);
        }
    }

    private static void loadPack(IModInfo mod, Object value) {
        if (value instanceof String string) {
            PACKS.add(new ResourcePack(mod, string, null, false));
        } else if (value instanceof UnmodifiableConfig config) {
            Map<String, Object> map = config.valueMap();
            String name = getOrThrow(map, "name");
            String description = (String) map.get("description");
            boolean required = (boolean) map.getOrDefault("required", false);
            PACKS.add(new ResourcePack(mod, name, description, required));
        }
    }

    @SuppressWarnings({"unchecked", "SameParameterValue"})
    private static <T> T getOrThrow(Map<?, ?> map, String id) {
        if (!map.containsKey(id)) throw new IllegalStateException("Missing key: " + id);
        return (T) map.get(id);
    }

    public static void onRegisterPackFinders(AddPackFindersEvent event) {
        for (ResourcePack resourcePack : PACKS) {
            try {
                Path path = resourcePack.mod().getOwningFile()
                        .getFile().findResource("resourcepacks/" + resourcePack.name());

                if (!Files.isDirectory(path.resolve(event.getPackType().getDirectory()))) continue;

                final String id = new ResourceLocation(resourcePack.mod().getModId(), resourcePack.name()).toString();
                final Pack.ResourcesSupplier supplier = new PathPackResources.PathResourcesSupplier(path, false);
                final Pack.Info info = getInfo(id, supplier, event.getPackType(), resourcePack.required());

                final Pack pack = Pack.create(
                        "builtin/" + id,
                        createDescription(resourcePack.description(), resourcePack.name()),
                        resourcePack.required(),
                        new PathPackResources.PathResourcesSupplier(path, false),
                        info,
                        Pack.Position.TOP,
                        resourcePack.required(),
                        PackSource.create(PackSource.NO_DECORATION, resourcePack.required())
                );

                event.addRepositorySource((source) -> source.accept(pack));
            } catch (Exception ignored) {
                Constants.LOGGER.error("Resourceful Lib failed to init resource pack for mod: " + resourcePack.mod().getDisplayName());
            }
        }
    }

    private static Pack.Info getInfo(String id, Pack.ResourcesSupplier supplier, PackType type, boolean hidden) {
        if (!hidden) {
            Pack.Info info = Pack.readPackInfo(id, supplier, SharedConstants.getCurrentVersion().getPackVersion(type));
            if (info != null) {
                return info;
            }
        }
        return new Pack.Info(CommonComponents.EMPTY, PackCompatibility.COMPATIBLE, FeatureFlagSet.of(), List.of(), hidden);
    }

    private static Component createDescription(@Nullable String description, String name) {
        if (description != null) {
            return Component.literal(description);
        }
        return Component.translatableWithFallback("resourcefullib.resourcepack." + name, name);
    }

    private record ResourcePack(
            IModInfo mod,
            String name,
            @Nullable String description,
            boolean required
    ) {

    }
}
