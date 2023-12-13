package ua.klymenko.DAO.EntityDAOImpl.Observer.Listeners;

import ua.klymenko.DAO.EntityDAOImpl.Observer.EventListener;
import ua.klymenko.entity.Lesson;

public class LessonListener implements EventListener<Lesson> {
    @Override
    public void update(Lesson entity) {
        System.out.println("{{OBSERVER}} LESSON UPDATED - " + entity);
    }

    @Override
    public void remove(Lesson entity) {
        System.out.println("{{OBSERVER}} LESSON REMOVED - " + entity);
    }

    @Override
    public void add(Lesson entity) {
        System.out.println("{{OBSERVER}} LESSON ADDED - " + entity);
    }
}
