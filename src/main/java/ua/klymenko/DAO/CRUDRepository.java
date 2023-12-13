package ua.klymenko.DAO;

import java.util.List;

public interface CRUDRepository<T> {
    String insert(T entity);
    boolean update(T entity);
    boolean delete(String id);
    List<T> findAll();
    T findById(String id);
}
