package ua.klymenko.DAO.Factory;


import ua.klymenko.DAO.EntityDAO.HomeworkDAO;
import ua.klymenko.DAO.EntityDAO.LessonDAO;
import ua.klymenko.DAO.EntityDAO.UserDAO;
import ua.klymenko.DAO.EntityDAOImpl.Observer.EventManager;

public interface DAOFactory {
    UserDAO getUserDAO(EventManager eventManager);

    LessonDAO getLessonDAO(EventManager eventManager);


    HomeworkDAO getHomeworkDAO(EventManager eventManager);
}
