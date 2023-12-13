package ua.klymenko.Observer;

public interface Observer<T> {
    void onUpdate(T entity);
    void onRemove(T entity);
    void onAdd(T entity);
}
