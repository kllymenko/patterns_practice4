package ua.klymenko.Observer.Notifiers;

import ua.klymenko.Observer.Observer;
import ua.klymenko.entity.Homework;

public class HomeworkNotifier implements Observer<Homework> {
    @Override
    public void onUpdate(Homework entity) {
        System.out.println("{{OBSERVER}} HOMEWORK UPDATED - " + entity);
    }

    @Override
    public void onRemove(Homework entity) {
        System.out.println("{{OBSERVER}} HOMEWORK REMOVED - " + entity);
    }

    @Override
    public void onAdd(Homework entity) {
        System.out.println("{{OBSERVER}} HOMEWORK ADDED - " + entity);
    }
}
