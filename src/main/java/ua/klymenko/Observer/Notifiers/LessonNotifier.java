package ua.klymenko.Observer.Notifiers;

import ua.klymenko.Observer.Observer;
import ua.klymenko.entity.Lesson;

public class LessonNotifier implements Observer<Lesson> {
    @Override
    public void onUpdate(Lesson entity) {
        System.out.println("{{OBSERVER}} LESSON UPDATED - " + entity);
    }

    @Override
    public void onRemove(Lesson entity) {
        System.out.println("{{OBSERVER}} LESSON REMOVED - " + entity);
    }

    @Override
    public void onAdd(Lesson entity) {
        System.out.println("{{OBSERVER}} LESSON ADDED - " + entity);
    }
}
