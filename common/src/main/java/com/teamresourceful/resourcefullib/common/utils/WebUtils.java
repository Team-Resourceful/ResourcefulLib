package com.teamresourceful.resourcefullib.common.utils;

import com.google.gson.JsonObject;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public final class WebUtils {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private WebUtils() throws UtilityClassException {
        throw new UtilityClassException();
    }

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
        return GsonHelpers.parseJson(get(url))
                .orElse(null);
    }
}
