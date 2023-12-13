package ua.klymenko.DAO.EntityDAOImpl.Observer;

public interface EventListener<T> {
    void update(T entity);
    void remove(T entity);
    void add(T entity);
}
