package com.teamresourceful.resourcefullib.client.sysinfo.defaults;

import com.teamresourceful.resourcefullib.client.sysinfo.SystemInfo;
import com.teamresourceful.resourcefullib.client.sysinfo.SystemInfoBuilder;

import java.util.function.Consumer;

public record JavaInfo() implements Consumer<SystemInfoBuilder> {

    public static void register() {
        SystemInfo.addBuilder("Java", new JavaInfo());
    }

    @Override
    public void accept(SystemInfoBuilder builder) {
        builder.append("Java Version", System.getProperty("java.version"));
        builder.append("Java Vendor", System.getProperty("java.vendor"));
        builder.append(
                "JVM Version", "%s (%s), %s",
                System.getProperty("java.vm.name"),
                System.getProperty("java.vm.info"),
                System.getProperty("java.vm.vendor")
        );
    }
}
