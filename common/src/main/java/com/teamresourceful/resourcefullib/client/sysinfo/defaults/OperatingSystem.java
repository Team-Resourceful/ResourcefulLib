package com.teamresourceful.resourcefullib.client.sysinfo.defaults;

import com.teamresourceful.resourcefullib.client.sysinfo.SystemInfo;
import com.teamresourceful.resourcefullib.client.sysinfo.SystemInfoBuilder;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import oshi.hardware.GlobalMemory;
import oshi.hardware.GraphicsCard;
import oshi.hardware.PhysicalMemory;
import oshi.util.FormatUtil;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public record OperatingSystem(oshi.SystemInfo info) implements Consumer<SystemInfoBuilder> {

    private static final Pattern INFO_PATTERN = Pattern.compile("(.*)=(.*)");

    public static void register() {
        SystemInfo.addBuilder("System", new OperatingSystem(new oshi.SystemInfo()));
    }

    private void appendGPU(SystemInfoBuilder builder, List<GraphicsCard> gpus) {
        int i = 0;
        for (GraphicsCard gpu : gpus) {
            builder.append("GPU #" + i, gpu.getName());

            for (String info : gpu.getVersionInfo().split(", ")) {
                var matcher = INFO_PATTERN.matcher(info);
                if (matcher.matches()) {
                    builder.append("GPU #%d %s".formatted(i, matcher.group(1)), matcher.group(2));
                } else {
                    builder.append("GPU #%d Version Info".formatted(i), info);
                }
            }
            builder.append("GPU #%d VRAM".formatted(i), FormatUtil.formatBytes(gpu.getVRam()));
            i++;
        }
    }

    private void appendMemorySlots(SystemInfoBuilder builder, List<PhysicalMemory> slots) {
        int i = 0;
        for (PhysicalMemory memory : slots) {
            builder.append("Memory Slot #" + i, "%s (%s)", memory.getMemoryType(), FormatUtil.formatHertz(memory.getClockSpeed()));
            builder.append("Memory Slot #" + i + " Capacity", FormatUtil.formatBytes(memory.getCapacity()));
            i++;
        }
    }

    private void appendMemory(SystemInfoBuilder builder, GlobalMemory memory) {
        long current = memory.getAvailable();
        long total = memory.getTotal();
        builder.append("RAM", "%s/%s", FormatUtil.formatBytes(current), FormatUtil.formatBytes(total));
    }

    @Override
    public void accept(SystemInfoBuilder builder) {
        var hardware = info.getHardware();
        var processor = hardware.getProcessor();
        var window = Minecraft.getInstance().getWindow();
        builder.append("OS", System.getProperty("os.name"));
        builder.append("CPU", processor.getProcessorIdentifier().getName());
        builder.append("Display", "%dx%d (%s)", window.getWidth(), window.getHeight(), GL11.glGetString(GL11.GL_VENDOR));
        builder.append("Language", Locale.getDefault().getDisplayLanguage());
        appendGPU(builder, hardware.getGraphicsCards());
        appendMemory(builder, hardware.getMemory());
        appendMemorySlots(builder, hardware.getMemory().getPhysicalMemory());
    }
}
