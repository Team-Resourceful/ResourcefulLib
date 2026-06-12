package com.teamresourceful.resourcefullib.client.sysinfo.defaults;

import com.teamresourceful.resourcefullib.client.sysinfo.SystemInfo;
import com.teamresourceful.resourcefullib.client.sysinfo.SystemInfoBuilder;
import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfo;
import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfoUtils;
import net.minecraft.Optionull;
import net.minecraft.SharedConstants;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.function.Consumer;

public record MinecraftInfo() implements Consumer<SystemInfoBuilder> {

    private static final DecimalFormat DECIMAL_FORMAT = Util.make(new DecimalFormat("########0.000"), (decimalFormat) ->
        decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT))
    );
    public static void register() {
        SystemInfo.addBuilder("Game", new MinecraftInfo());
    }

    private void tryAppendingFabric(SystemInfoBuilder builder) {
        if (ModInfoUtils.isModLoaded("fabric")) {
            ModInfo info = ModInfoUtils.getModInfo("fabric");
            builder.append("Mod Loader", info.displayName());
            builder.append("Fabric Version", info.version());
        }
    }

    private void tryAppendingQuilt(SystemInfoBuilder builder) {
        if (ModInfoUtils.isModLoaded("quilt")) {
            ModInfo info = ModInfoUtils.getModInfo("quilt");
            builder.append("Mod Loader", info.displayName());
            builder.append("Quilt Version", info.version());
        }
    }

    private void tryAppendingForge(SystemInfoBuilder builder) {
        if (ModInfoUtils.isModLoaded("forge")) {
            ModInfo info = ModInfoUtils.getModInfo("forge");
            builder.append("Mod Loader", info.displayName());
            builder.append("Forge Version", info.version());
        }
    }

    private void tryAppendingNeoForge(SystemInfoBuilder builder) {
        if (ModInfoUtils.isModLoaded("neoforge")) {
            ModInfo info = ModInfoUtils.getModInfo("neoforge");
            builder.append("Mod Loader", info.displayName());
            builder.append("NeoForge Version", info.version());
        }
    }

    private static long mean(long[] values) {
        long sum = 0L;
        for (long v : values) {
            sum += v;
        }
        return sum / values.length;
    }

    private static String getPing(Minecraft mc) {
        var logger = mc.getDebugOverlay().getPingLogger();
        if (logger.size() == 0) {
            return "N/A";
        }

        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long sum = 0L;

        for (int i = 0; i < logger.size(); i++) {
            long sample = logger.get(i);
            min = Math.min(min, sample);
            max = Math.max(max, sample);
            sum += sample;
        }

        return "%.2f avg ms / %d min ms / %d max ms".formatted(
                sum / (double) logger.size(),
                min,
                max
        );
    }

    @Override
    public void accept(SystemInfoBuilder builder) {
        Minecraft mc = Minecraft.getInstance();

        builder.append("Minecraft Version", SharedConstants.getCurrentVersion().name());
        builder.append("Client Brand", ClientBrandRetriever.getClientModName());
        builder.append("Language", mc.getLanguageManager().getSelected());
        tryAppendingFabric(builder);
        tryAppendingQuilt(builder);
        tryAppendingForge(builder);
        tryAppendingNeoForge(builder);
        builder.append("FPS", Minecraft.getInstance().getFps());
        builder.append("TPS", Optionull.mapOrDefault(mc.getSingleplayerServer(),
                server -> DECIMAL_FORMAT.format(Math.min(1000.0 / (mean(server.getTickTimesNanos()) * 1.0E-6D), 20)),
                "N/A"
        ));
        builder.append("Ping", Optionull.mapOrDefault(mc.getConnection(),
                $ -> getPing(mc),
                "N/A"
        ));
        builder.append("Connection", mc.isMultiplayerServer() ? (mc.isLocalServer() ? "Singleplayer Lan" : "Multiplayer") : "Singleplayer");
        builder.append("Loaded Mods", ModInfoUtils.getLoadedMods());
    }
}
