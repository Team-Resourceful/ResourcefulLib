package com.teamresourceful.resourcefullib.common.utils;

import com.google.gson.JsonObject;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import com.teamresourceful.resourcefullib.common.lib.Constants;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class GsonHelpers {

    private GsonHelpers() throws UtilityClassException {
        throw new UtilityClassException();
    }

    public static Optional<JsonObject> parseJson(@Nullable String json) {
        try {
            return Optional.of(Constants.GSON.fromJson(json, JsonObject.class));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
