package com.teamresourceful.resourcefullib.common.utils;

import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

import java.util.function.Predicate;
import java.util.function.Supplier;

public final class CommonUtils {

    private CommonUtils() throws UtilityClassException {
        throw new UtilityClassException();
    }

    /**
     * Generated a unique value dependent on the validator
     */
    public static <T> T generate(Predicate<T> validator, Supplier<T> getter) {
        T value;
        do {
            value = getter.get();
        } while (!validator.test(value));
        return value;
    }

    /**
     * Create a translation with a fallback to the translated version on loaded on the server.
     */
    public static Component serverTranslatable(String key, Object... args) {
        if (args.length == 0) {
            return Component.translatableWithFallback(key, Language.getInstance().getOrDefault(key, key));
        } else {
            return Component.translatableWithFallback(key, Language.getInstance().getOrDefault(key, key), args);
        }
    }
}