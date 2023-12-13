package ua.klymenko.DAO.EntityDAO;



import ua.klymenko.DAO.CRUDRepository;
import ua.klymenko.entity.Homework;

import java.sql.Timestamp;

public interface HomeworkDAO extends CRUDRepository<Homework> {
    public boolean updateDueDateTime(Homework entity, Timestamp dueDateTime);
}
