package ua.klymenko.Proxy;

import ua.klymenko.DAO.EntityDAO.UserDAO;
import ua.klymenko.entity.User;
import ua.klymenko.entity.enums.Role;

import java.util.List;

public class UserDAOProxy implements UserDAO {

    private final UserDAO userDAO;
    private final Role role;

    public UserDAOProxy(UserDAO userDAO, Role role) {
        this.userDAO = userDAO;
        this.role = role;
    }

    @Override
    public String insert(User entity) {
        if(role == Role.TEACHER){
            return userDAO.insert(entity);
        }
        System.out.println("ВІДХИЛЕНО! Додавання/зміна/видалення доступні лише викладачам");
        return null;
    }

    @Override
    public boolean update(User entity) {
        if(role == Role.TEACHER){
            return userDAO.update(entity);
        }
        System.out.println("ВІДХИЛЕНО! Додавання/зміна/видалення доступні лише викладачам");
        return false;
    }

    @Override
    public boolean delete(String id) {
        if(role == Role.TEACHER){
            return userDAO.delete(id);
        }
        System.out.println("ВІДХИЛЕНО! Додавання/зміна/видалення доступні лише викладачам");
        return false;
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public User findById(String id) {
        return userDAO.findById(id);
    }

    @Override
    public boolean updatePassword(User entity, String newPassword) {
        if(role == Role.TEACHER){
            return userDAO.updatePassword(entity, newPassword);
        }
        System.out.println("ВІДХИЛЕНО! Додавання/зміна/видалення доступні лише викладачам");
        return false;
    }
}
