package com.teamresourceful.resourcefullib.common.utils;

import org.jetbrains.annotations.ApiStatus;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Use mixins and extensible enums to create new enum values at runtime.
 */
@ApiStatus.Obsolete
@SuppressWarnings({"unchecked", "unused"})
public class EnumBuilder<T extends Enum<T>> {

    private static final Field ENUM_CONSTANTS = UnsafeUtils.getField(Class.class, field -> field.getName().equals("enumConstants"));
    private static final Field ENUM_CONSTANT_DIRECTORY = UnsafeUtils.getField(Class.class, field -> field.getName().equals("enumConstantDirectory"));

    private static final Predicate<Field> IS_SYNTHETIC = Field::isSynthetic;
    private static final Predicate<Field> IS_ARRAY = field -> field.getType().isArray();
    private static final Predicate<Field> IS_STATIC = field -> Modifier.isStatic(field.getModifiers());
    private static final Predicate<Field> NAMED_VALUES = field -> field.getName().contains("VALUES");
    private static final Predicate<Field> IS_VALUES_FIELD = IS_SYNTHETIC.and(IS_STATIC).and(IS_ARRAY).and(NAMED_VALUES);

    private final Class<T> enumClass;
    private final String id;
    private final List<Object> args = new ArrayList<>();
    private final List<Class<?>> types = new ArrayList<>();

    public EnumBuilder(Class<T> enumClass, String id) {
        this.enumClass = enumClass;
        this.id = id;
    }

    public static <T extends Enum<T>> EnumBuilder<T> of(Class<T> enumClass, String id) {
        return new EnumBuilder<>(enumClass, id);
    }

    public EnumBuilder<T> withArg(Class<?> type, Object arg) {
        this.args.add(arg);
        this.types.add(type);
        return this;
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    public T build() throws Throwable {
        Class<?>[] argTypes = new Class<?>[types.size() + 2];
        argTypes[0] = String.class;
        argTypes[1] = int.class;
        System.arraycopy(types.toArray(), 0, argTypes, 2, types.size());

        Object[] newArgs = new Object[args.size() + 2];
        newArgs[0] = id;
        newArgs[1] = enumClass.getEnumConstants().length;
        System.arraycopy(args.toArray(), 0, newArgs, 2, args.size());

        verify(argTypes, newArgs);

        return EnumBuilder.create(enumClass, newArgs, argTypes);
    }

    private void verify(Class<?>[] argTypes, Object[] newArgs) {
        if (argTypes.length != newArgs.length) {
            throw new IllegalArgumentException("Argument types and arguments must be the same length");
        }
    }

    //region Unsafe operations for enum creation

    private static <T extends Enum<T>> T create(Class<T> enumClass, Object[] args, Class<?>[] types) throws Throwable {
        Constructor<T> constructor = enumClass.getDeclaredConstructor(types);
        constructor.setAccessible(true);
        T output = (T) MethodHandles.lookup()
                .unreflectConstructor(constructor)
                .invokeWithArguments(args);

        Field valuesField = UnsafeUtils.getField(enumClass, field -> IS_VALUES_FIELD.test(field) && field.getType().getComponentType().equals(enumClass));
        addArrayValue(valuesField, enumClass, output);
        UnsafeUtils.setField(enumClass, ENUM_CONSTANT_DIRECTORY, null);
        UnsafeUtils.setField(enumClass, ENUM_CONSTANTS, null);
        return output;
    }

    public static <T> void addArrayValue(Field data, Class<T> object, T arrayEntry) {
        Object base = UnsafeUtils.unsafe().staticFieldBase(data);
        long offset = UnsafeUtils.unsafe().staticFieldOffset(data);
        T[] array = (T[]) UnsafeUtils.unsafe().getObject(base, offset);
        T[] newArray = (T[]) Array.newInstance(object, array.length + 1);
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = arrayEntry;
        UnsafeUtils.unsafe().putObject(base, offset, newArray);
    }

    //endregion
}