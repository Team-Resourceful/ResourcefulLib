package com.teamresourceful.resourcefullib.common.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class WebUtils {

    private static final Gson GSON = new Gson();
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Nullable
    public static String get(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI(url))
                    .GET()
                    .version(HttpClient.Version.HTTP_2)
                    .header("User-Agent", "Minecraft Mod")
                    .build();

            HttpResponse<String> send = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            return send.body();
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public static JsonObject getJson(String url) {
        String data = get(url);
        if (data == null) return null;
        try {
            return GSON.fromJson(data, JsonObject.class);
        } catch (Exception e) {
            return null;
        }
    }
}
