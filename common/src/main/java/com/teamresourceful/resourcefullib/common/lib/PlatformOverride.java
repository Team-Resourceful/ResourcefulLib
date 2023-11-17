package com.teamresourceful.resourcefullib.common.lib;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark methods that are overridden for a specific platform.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({java.lang.annotation.ElementType.METHOD})
@ApiStatus.Internal
public @interface PlatformOverride {

    /**
     * @return The platform this method is overridden for.
     */
    String value();
}
