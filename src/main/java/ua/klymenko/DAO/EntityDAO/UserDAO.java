package ua.klymenko.DAO.EntityDAO;


import ua.klymenko.DAO.CRUDRepository;
import ua.klymenko.entity.User;

public interface UserDAO extends CRUDRepository<User> {
    public boolean updatePassword(User entity, String newPassword);
}
