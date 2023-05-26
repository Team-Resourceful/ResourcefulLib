package com.teamresourceful.resourcefullib.common.utils;

import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public final class UnsafeUtils {

    private UnsafeUtils() throws UtilityClassException {
        throw new UtilityClassException();
    }

    private static final Unsafe UNSAFE;
    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe)theUnsafe.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Unable to capture unsafe", e);
        }
    }

    public static boolean hasField(Object instance, String fieldName) {
        try {
            instance.getClass().getDeclaredField(fieldName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void setField(Object instance, String fieldName, Object value) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            UNSAFE.putObject(instance, UNSAFE.objectFieldOffset(field), value);
        } catch (Exception e) {
            throw new RuntimeException("Unable to set field", e);
        }
    }

    public static Object getStaticField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            long offset = UNSAFE.staticFieldOffset(field);
            Object base = UNSAFE.staticFieldBase(field);
            return UNSAFE.getObject(base, offset);
        } catch (Exception e) {
            throw new RuntimeException("Unable to get static field", e);
        }
    }

    public static void setStaticField(Class<?> clazz, String fieldName, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            long offset = UNSAFE.staticFieldOffset(field);
            Object base = UNSAFE.staticFieldBase(field);
            UNSAFE.putObject(base, offset, value);
        } catch (Exception e) {
            throw new RuntimeException("Unable to set static field", e);
        }
    }

    public static Unsafe unsafe() {
        return UNSAFE;
    }
}