package ua.klymenko.DAO.EntityDAOImpl.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    private Map<String, List<EventListener>> listeners = new HashMap<>();

    public <T> void subscribe(String eventType, EventListener listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public <T> void unsubscribe(String eventType, EventListener<T> listener) {
        listeners.getOrDefault(eventType, new ArrayList<>()).remove(listener);
    }

    public <T> void notifyEntityAdded(String eventType, T entity) {
        List<EventListener> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            for (EventListener<?> listener : eventListeners) {
                ((EventListener<T>) listener).add(entity);
            }
        }
    }
    public <T> void notifyEntityRemoved(String eventType, T entity) {
        List<EventListener> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            for (EventListener<?> listener : eventListeners) {
                ((EventListener<T>) listener).remove(entity);
            }
        }
    }
    public <T> void notifyEntityUpdated(String eventType, T entity) {
        List<EventListener> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            for (EventListener<?> listener : eventListeners) {
                ((EventListener<T>) listener).update(entity);
            }
        }
    }
}
