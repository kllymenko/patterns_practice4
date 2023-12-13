package ua.klymenko.DAO.EntityDAOImpl.Observer.Listeners;

import ua.klymenko.DAO.EntityDAOImpl.Observer.EventListener;
import ua.klymenko.entity.Homework;

public class HomeworkListener implements EventListener<Homework> {
    @Override
    public void update(Homework entity) {
        System.out.println("{{OBSERVER}} HOMEWORK UPDATED - " + entity);
    }

    @Override
    public void remove(Homework entity) {
        System.out.println("{{OBSERVER}} HOMEWORK REMOVED - " + entity);
    }

    @Override
    public void add(Homework entity) {
        System.out.println("{{OBSERVER}} HOMEWORK ADDED - " + entity);
    }
}
