package ua.klymenko.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Publisher {
    private Map<String, List<Observer<?>>> listeners = new HashMap<>();

    public <T> void subscribe(String eventType, Observer listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }
    public <T> void subscribeToAll(String eventType, Observer<T> listener) {
        subscribe(eventType + "Add", listener);
        subscribe(eventType + "Remove", listener);
        subscribe(eventType + "Update", listener);
    }
    public <T> void unsubscribe(String eventType, Observer<T> listener) {
        listeners.getOrDefault(eventType, new ArrayList<>()).remove(listener);
    }


    public <T> void notifyAdd(String eventType, T entity) {
        List<Observer<?>> observers = listeners.get(eventType);
        for (Observer<?> listener : observers) {
            ((Observer<T>) listener).onAdd(entity);
        }
    }

    public <T> void notifyRemove(String eventType, T entity) {
        List<Observer<?>> observers = listeners.get(eventType);
        for (Observer<?> listener : observers) {
            ((Observer<T>) listener).onRemove(entity);
        }
    }

    public <T> void notifyUpdate(String eventType, T entity) {
        List<Observer<?>> observers = listeners.get(eventType);
        for (Observer<?> listener : observers) {
            ((Observer<T>) listener).onUpdate(entity);
        }
    }
}
