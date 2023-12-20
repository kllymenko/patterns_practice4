package ua.klymenko.Memento;

import ua.klymenko.DAO.CRUDRepository;
import ua.klymenko.entity.Homework;

import java.util.ArrayList;
import java.util.List;

public interface MementoManager<T> {
    public void add(T entity, CRUDRepository<T> dao);
    public void update(T entity, CRUDRepository<T> dao);
    public void undoUpdate(T entity, CRUDRepository<T> dao);
}
