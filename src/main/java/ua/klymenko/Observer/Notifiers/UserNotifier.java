package ua.klymenko.Observer.Notifiers;

import ua.klymenko.Observer.Observer;
import ua.klymenko.entity.User;

public class UserNotifier implements Observer<User> {
    @Override
    public void onUpdate(User entity) {
        System.out.println("{{OBSERVER}} USER UPDATED - " + entity);
    }

    @Override
    public void onRemove(User entity) {
        System.out.println("{{OBSERVER}} USER REMOVED - " + entity);
    }

    @Override
    public void onAdd(User entity) {
        System.out.println("{{OBSERVER}} USER ADDED - " + entity);
    }
}
