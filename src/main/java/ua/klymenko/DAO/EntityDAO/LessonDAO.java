package ua.klymenko.DAO.EntityDAO;


import ua.klymenko.DAO.CRUDRepository;
import ua.klymenko.entity.Lesson;

public interface LessonDAO extends CRUDRepository<Lesson> {
    public boolean updateName(Lesson entity, String newName);
}
