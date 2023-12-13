package ua.klymenko.DAO.Factory;


import ua.klymenko.DAO.EntityDAO.HomeworkDAO;
import ua.klymenko.DAO.EntityDAO.LessonDAO;
import ua.klymenko.DAO.EntityDAO.UserDAO;
import ua.klymenko.Observer.Publisher;

public interface DAOFactory {
    UserDAO getUserDAO(Publisher publisher);

    LessonDAO getLessonDAO(Publisher publisher);


    HomeworkDAO getHomeworkDAO(Publisher publisher);
}
