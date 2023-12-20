package ua.klymenko.Proxy;

import ua.klymenko.DAO.EntityDAO.HomeworkDAO;
import ua.klymenko.entity.Homework;
import ua.klymenko.entity.enums.Role;

import java.sql.Timestamp;
import java.util.List;

public class HomeworkDAOProxy implements HomeworkDAO {
    private final HomeworkDAO homeworkDAO;
    private final Role role;

    public HomeworkDAOProxy(HomeworkDAO homeworkDAO, Role role) {
        this.homeworkDAO = homeworkDAO;
        this.role = role;
    }
    @Override
    public String insert(Homework entity) {
        if(role == Role.TEACHER){
           return homeworkDAO.insert(entity);
        }
        System.out.println("ВІДХИЛЕНО! Додавання/зміна/видалення доступні лише викладачам");
        return null;
    }

    @Override
    public boolean update(Homework entity) {
        if(role == Role.TEACHER){
            return homeworkDAO.update(entity);
        }
        System.out.println("ВІДХИЛЕНО! Додавання/зміна/видалення доступні лише викладачам");
        return false;
    }

    @Override
    public boolean delete(String id) {
        if(role == Role.TEACHER){
            return homeworkDAO.delete(id);
        }
        System.out.println("ВІДХИЛЕНО! Додавання/зміна/видалення доступні лише викладачам");
        return false;
    }

    @Override
    public List<Homework> findAll() {
        return homeworkDAO.findAll();
    }

    @Override
    public Homework findById(String id) {
        return homeworkDAO.findById(id);
    }

    @Override
    public boolean updateDueDateTime(Homework entity, Timestamp dueDateTime) {
        if(role == Role.TEACHER){
            return homeworkDAO.updateDueDateTime(entity, dueDateTime);
        }
        System.out.println("ВІДХИЛЕНО! Додавання/зміна/видалення доступні лише викладачам");
        return false;
    }
}
