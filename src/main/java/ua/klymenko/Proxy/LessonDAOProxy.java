package ua.klymenko.Proxy;

import ua.klymenko.DAO.EntityDAO.LessonDAO;
import ua.klymenko.entity.Lesson;
import ua.klymenko.entity.enums.Role;

import java.util.List;

public class LessonDAOProxy implements LessonDAO {
    private final LessonDAO lessonDAO;
    private final Role role;

    public LessonDAOProxy(LessonDAO lessonDAO, Role role) {
        this.lessonDAO = lessonDAO;
        this.role = role;
    }

    @Override
    public String insert(Lesson entity) {
        if(role == Role.TEACHER){
            return lessonDAO.insert(entity);
        }
        System.out.println("ВІДХИЛЕНО! Додавання/зміна/видалення доступні лише викладачам");
        return null;
    }

    @Override
    public boolean update(Lesson entity) {
        if(role == Role.TEACHER){
            return lessonDAO.update(entity);
        }
        System.out.println("ВІДХИЛЕНО! Додавання/зміна/видалення доступні лише викладачам");
        return false;
    }

    @Override
    public boolean delete(String id) {
        if(role == Role.TEACHER){
            return lessonDAO.delete(id);
        }
        System.out.println("ВІДХИЛЕНО! Додавання/зміна/видалення доступні лише викладачам");
        return false;
    }

    @Override
    public List<Lesson> findAll() {
        return lessonDAO.findAll();
    }

    @Override
    public Lesson findById(String id) {
        return lessonDAO.findById(id);
    }

    @Override
    public boolean updateName(Lesson entity, String newName) {
        if(role == Role.TEACHER){
            return lessonDAO.updateName(entity, newName);
        }
        System.out.println("ВІДХИЛЕНО! Додавання/зміна/видалення доступні лише викладачам");
        return false;
    }
}
