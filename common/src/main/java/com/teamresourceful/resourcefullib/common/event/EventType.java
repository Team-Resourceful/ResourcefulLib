package com.teamresourceful.resourcefullib.common.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EventType<T> {
    private final List<Consumer<T>> listeners = new ArrayList<>();

    public void register(Consumer<T> listener) {
        listeners.add(listener);
    }

    public void post(T event) {
        for (Consumer<T> listener : listeners) {
            listener.accept(event);
        }
    }
}
