package com.teamresourceful.resourcefullib.common.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class Constants {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Gson GSON = new Gson();
    public static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
}
