package ua.klymenko.DAO.EntityDAOImpl.Observer.Listeners;

import ua.klymenko.DAO.EntityDAOImpl.Observer.EventListener;
import ua.klymenko.entity.User;

public class UserListener implements EventListener<User> {
    @Override
    public void update(User entity) {
        System.out.println("{{OBSERVER}} USER UPDATED - " + entity);
    }

    @Override
    public void remove(User entity) {
        System.out.println("{{OBSERVER}} USER REMOVED - " + entity);
    }

    @Override
    public void add(User entity) {
        System.out.println("{{OBSERVER}} USER ADDED - " + entity);
    }
}
