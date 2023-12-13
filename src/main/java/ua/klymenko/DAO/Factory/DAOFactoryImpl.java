package ua.klymenko.DAO.Factory;




import ua.klymenko.DAO.ConnectionManager;
import ua.klymenko.DAO.ConnectionProperties;
import ua.klymenko.DAO.EntityDAO.HomeworkDAO;
import ua.klymenko.DAO.EntityDAO.LessonDAO;
import ua.klymenko.DAO.EntityDAO.UserDAO;
import ua.klymenko.DAO.EntityDAOImpl.MySQLHomeworkDAOImpl;
import ua.klymenko.DAO.EntityDAOImpl.MySQLLessonDAOImpl;
import ua.klymenko.DAO.EntityDAOImpl.MySQLUserDAOImpl;
import ua.klymenko.DAO.EntityDAOImpl.Observer.EventManager;

import java.sql.Connection;

public class DAOFactoryImpl implements DAOFactory{

    private ConnectionManager connectionManager;

    public DAOFactoryImpl() {
        ConnectionProperties connectionProperties = new ConnectionProperties();
        connectionManager = ConnectionManager.getInstance(connectionProperties);
    }


    @Override
    public UserDAO getUserDAO(EventManager eventManager) {
        Connection connection = connectionManager.getConnection();
        return new MySQLUserDAOImpl(connection, eventManager);
    }

    @Override
    public LessonDAO getLessonDAO(EventManager eventManager) {
        Connection connection = connectionManager.getConnection();
        return new MySQLLessonDAOImpl(connection, eventManager);
    }

    @Override
    public HomeworkDAO getHomeworkDAO(EventManager eventManager) {
        Connection connection = connectionManager.getConnection();
        return new MySQLHomeworkDAOImpl(connection, eventManager);
    }
}
