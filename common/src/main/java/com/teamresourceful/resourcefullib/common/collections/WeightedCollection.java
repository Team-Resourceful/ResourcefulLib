package com.teamresourceful.resourcefullib.common.collections;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class WeightedCollection<E> implements Collection<E> {

    private final NavigableMap<Double, E> map = new TreeMap<>();
    private final Random random;
    private double total = 0;

    public WeightedCollection() {
        this(new Random());
    }

    public WeightedCollection(Random random) {
        this.random = random;
    }

    public WeightedCollection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(Math.min(total, Double.MAX_VALUE), result);
        return this;
    }

    public E get(int index) {
        Double key = new LinkedList<>(map.keySet()).get(index);
        return map.get(key);
    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }

    public NavigableMap<Double, E> getMap(){
        return map;
    }

    public double getAdjustedWeight(double weight) {
        return weight / total;
    }

    public void forEachWithSelf(BiConsumer<WeightedCollection<E>, ? super E> action) {
        forEach(element -> action.accept(this, element));
    }

    public Stream<E> stream() {
        return map.values().stream();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        try {
            return map.containsValue(o);
        } catch (Exception e) {
            return false;
        }
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return map.values().iterator();
    }

    @Override
    public Object @NotNull [] toArray() {
        return map.values().toArray();
    }

    @Override
    public <T> T @NotNull [] toArray(T @NotNull [] a) {
        return map.values().toArray(a);
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException("Default add is not supported, use add that takes in a weight.");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Removing not supported.");
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        throw new UnsupportedOperationException("Default add is not supported, use add that takes in a weight.");
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException("Removing not supported.");
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException("Removing not supported.");
    }

    @Override
    public void clear() {
        map.clear();
        total = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        return o instanceof WeightedCollection && map.equals(((WeightedCollection<?>) o).map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, map);
    }

    public static <T> WeightedCollection<T> of(Collection<T> collection, ToDoubleFunction<T> weightGetter) {
        return collection.stream().collect(getCollector(weightGetter));
    }

    public static <T> Collector<T, ?, WeightedCollection<T>> getCollector(ToDoubleFunction<T> weightGetter) {
        return Collector.of(WeightedCollection::new, (c, t) -> c.add(weightGetter.applyAsDouble(t), t), (left, right) -> {
            left.forEach(item -> right.add(weightGetter.applyAsDouble(item), item));
            return right;
        });
    }
}
