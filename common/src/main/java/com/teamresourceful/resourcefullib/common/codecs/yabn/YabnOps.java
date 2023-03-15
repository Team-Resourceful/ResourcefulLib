package com.teamresourceful.resourcefullib.common.codecs.yabn;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.teamresourceful.yabn.YabnCompressor;
import com.teamresourceful.yabn.elements.YabnArray;
import com.teamresourceful.yabn.elements.YabnElement;
import com.teamresourceful.yabn.elements.YabnObject;
import com.teamresourceful.yabn.elements.YabnPrimitive;
import com.teamresourceful.yabn.elements.primitives.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class YabnOps implements DynamicOps<YabnElement> {

    public static final YabnOps INSTANCE = new YabnOps(false);
    public static final YabnOps COMPRESSED = new YabnOps(true);

    private final boolean compressed;

    protected YabnOps(boolean compressed) {
        this.compressed = compressed;
    }

    @Override
    public YabnElement empty() {
        return YabnPrimitive.ofNull();
    }

    @Override
    public <U> U convertTo(DynamicOps<U> outOps, YabnElement input) {
        if (input instanceof YabnObject) return convertMap(outOps, input);
        else if (input instanceof YabnArray) return convertList(outOps, input);

        if (compressed) input = YabnCompressor.compress(input);
        if (input.isNull()) return outOps.empty();
        if (input instanceof YabnPrimitive primitive) {
            final PrimitiveContents contents = primitive.contents();
            if (contents instanceof StringContents stringContents) return outOps.createString(stringContents.value());
            else if (contents instanceof BooleanContents booleanContents) return outOps.createBoolean(booleanContents.value());
            else if (contents instanceof ByteContents byteContents) return outOps.createByte(byteContents.value());
            else if (contents instanceof ShortContents shortContents) return outOps.createShort(shortContents.value());
            else if (contents instanceof IntContents intContents) return outOps.createInt(intContents.value());
            else if (contents instanceof LongContents longContents) return outOps.createLong(longContents.value());
            else if (contents instanceof DoubleContents doubleContents) return outOps.createDouble(doubleContents.value());
            else if (contents instanceof FloatContents floatContents) return outOps.createFloat(floatContents.value());
            throw new IllegalStateException("Unknown primitive contents: " + contents);
        }
        throw new IllegalArgumentException("Unknown type: " + input.getClass());
    }

    @Override
    public DataResult<Number> getNumberValue(YabnElement input) {
        if (input instanceof YabnPrimitive primitive) {
            final PrimitiveContents contents = primitive.contents();
            if (contents instanceof PrimitiveNumberContents num) return DataResult.success(num.getValue());
            if (contents instanceof BooleanContents bool) return DataResult.success(bool.value() ? 1 : 0);
            if (compressed && contents instanceof StringContents str) {
                try {
                    return DataResult.success(Integer.parseInt(str.value()));
                } catch (final NumberFormatException e) {
                    return DataResult.error(() -> "Not a number: " + e + " " + input);
                }
            }
        }
        return DataResult.error(() -> "Not a number: " + input);
    }

    @Override
    public YabnElement createNumeric(Number i) {
        final BigDecimal value = new BigDecimal(i.toString());
        try {
            return YabnCompressor.compressNonFloatingNumber(value.longValueExact());
        } catch (final ArithmeticException e) {
            return YabnCompressor.compressFloatingNumber(value.doubleValue());
        }
    }

    @Override
    public DataResult<Boolean> getBooleanValue(YabnElement input) {
        if (input instanceof YabnPrimitive primitive) {
            final PrimitiveContents contents = primitive.contents();
            if (contents instanceof BooleanContents bool) return DataResult.success(bool.value());
            if (contents instanceof PrimitiveNumberContents num) return DataResult.success(num.getAsByte() != 0);
        }
        return DataResult.error(() -> "Not a boolean: " + input);
    }

    @Override
    public YabnElement createBoolean(boolean value) {
        return YabnPrimitive.ofBoolean(value);
    }

    @Override
    public DataResult<String> getStringValue(YabnElement input) {
        if (input instanceof YabnPrimitive primitive) {
            if (primitive.contents() instanceof StringContents contents) {
                return DataResult.success(contents.value());
            }
            if (compressed && primitive.contents() instanceof PrimitiveNumberContents contents) {
                return DataResult.success(contents.getValue().toString());
            }
        }
        return DataResult.error(() -> "Not a string: " + input);
    }

    @Override
    public YabnElement createString(String value) {
        return YabnPrimitive.ofString(value);
    }

    @Override
    public DataResult<YabnElement> mergeToList(YabnElement list, YabnElement value) {
        return mergeToList(list, List.of(value));
    }

    @Override
    public DataResult<YabnElement> mergeToList(YabnElement list, List<YabnElement> values) {
        if (!(list instanceof YabnArray) && list != empty()) return DataResult.error(() -> "Not a list: " + list);
        final YabnArray array = new YabnArray();
        if (list instanceof YabnArray oldArray) oldArray.elements().forEach(array::add);
        values.forEach(array::add);
        return DataResult.success(array);
    }

    @Override
    public DataResult<YabnElement> mergeToMap(YabnElement map, YabnElement key, YabnElement value) {
        if (!(map instanceof YabnObject) && map != empty()) return DataResult.error(() -> "Not a map: " + map, map);
        if (!(key instanceof YabnPrimitive primitive) || primitive.isNull() || !(primitive.contents() instanceof StringContents) && !compressed) {
            return DataResult.error(() -> "key is not a string: " + key, map);
        }

        final YabnObject object = new YabnObject();
        if (map instanceof YabnObject oldObject) oldObject.elements().forEach(object::put);
        object.elements().put(getAsString(primitive), value);
        return DataResult.success(object);
    }

    @Override
    public DataResult<YabnElement> mergeToMap(YabnElement map, MapLike<YabnElement> values) {
        if (!(map instanceof YabnObject) && map != empty()) {
            return DataResult.error(() -> "Not a map: " + map, map);
        }

        final YabnObject object = new YabnObject();
        if (map instanceof YabnObject oldObject) oldObject.elements().forEach(object::put);

        final List<YabnElement> missed = Lists.newArrayList();

        values.entries().forEach(entry -> {
            final YabnElement key = entry.getFirst();
            if (!(key instanceof YabnPrimitive primitive) || primitive.isNull() || !(primitive.contents() instanceof StringContents) && !compressed) {
                missed.add(key);
                return;
            }
            object.put(getAsString(primitive), entry.getSecond());
        });

        return !missed.isEmpty() ? DataResult.error(() -> "some keys are not strings: " + missed, object) : DataResult.success(object);
    }

    @Override
    public DataResult<Stream<Pair<YabnElement, YabnElement>>> getMapValues(YabnElement input) {
        if (input instanceof YabnObject object) {
            Stream<Pair<YabnElement, YabnElement>> output = object.elements()
                    .entrySet()
                    .stream()
                    .map(entry -> Pair.of(createString(entry.getKey()), entry.getValue().getOrNull()));
            return DataResult.success(output);
        }
        return DataResult.error(() -> "Not a YABN Object: " + input);
    }

    @Override
    public DataResult<Consumer<BiConsumer<YabnElement, YabnElement>>> getMapEntries(YabnElement input) {
        if (input instanceof YabnObject object) {
            return DataResult.success(c -> object.elements().forEach((key, value) -> c.accept(createString(key), value.getOrNull())));
        }
        return DataResult.error(() -> "Not a YABN Object: " + input);
    }

    @Override
    public DataResult<MapLike<YabnElement>> getMap(YabnElement input) {
        return input instanceof YabnObject object ? DataResult.success(new YabnObjectMapLike(object)) : DataResult.error(() -> "Not a YABN Object: " + input);
    }

    @Override
    public YabnElement createMap(Stream<Pair<YabnElement, YabnElement>> map) {
        final YabnObject object = new YabnObject();
        //noinspection OptionalGetWithoutIsPresent
        map.forEach(p -> object.put(getStringValue(p.getFirst()).result().get(), p.getSecond()));
        return object;
    }

    @Override
    public DataResult<Stream<YabnElement>> getStream(YabnElement input) {
        if (input instanceof YabnArray array) {
            return DataResult.success(array.elements().stream().map(YabnElement::getOrNull));
        }
        return DataResult.error(() -> "Not a YABN Array: " + input);
    }

    @Override
    public DataResult<Consumer<Consumer<YabnElement>>> getList(YabnElement input) {
        if (input instanceof YabnArray array) {
            return DataResult.success(c -> array.elements().forEach(e -> c.accept(e.getOrNull())));
        }
        return DataResult.error(() -> "Not a YABN Array: " + input);
    }

    @Override
    public YabnElement createList(Stream<YabnElement> input) {
        final YabnArray array = new YabnArray();
        input.forEach(array::add);
        return array;
    }

    @Override
    public YabnElement remove(YabnElement input, String key) {
        if (input instanceof YabnObject object) object.elements().remove(key);
        return input;
    }

    @Override
    public String toString() {
        return "YABN";
    }

    @Override
    public ListBuilder<YabnElement> listBuilder() {
        return new YabnArrayBuilder();
    }

    @Override
    public boolean compressMaps() {
        return compressed;
    }

    @Override
    public RecordBuilder<YabnElement> mapBuilder() {
        return new YabnRecordBuilder(this);
    }

    public static String getAsString(YabnPrimitive primitive) {
        final PrimitiveContents contents = primitive.contents();
        if (contents instanceof StringContents stringContents) return stringContents.value();
        else if (contents instanceof PrimitiveNumberContents numberContents) return numberContents.getValue().toString();
        else if (contents instanceof BooleanContents booleanContents) return booleanContents.value() ? "true" : "false";
        return null;
    }
}
