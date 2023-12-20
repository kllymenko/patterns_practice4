package ua.klymenko.Memento;

import ua.klymenko.DAO.CRUDRepository;
import ua.klymenko.entity.Homework;
import ua.klymenko.entity.Homework.HomeworkMemento;

public class HomeworkCaretaker implements MementoManager<Homework> {
    @Override
    public void add(Homework entity, CRUDRepository<Homework> dao) {
        entity.setHomeworkId(dao.insert(entity));
        Homework.HomeworkMemento memento = entity.new HomeworkMemento(entity);
        memento.saveToMemento();
    }

    @Override
    public void update(Homework entity, CRUDRepository<Homework> dao) {
        dao.update(entity);
        Homework.HomeworkMemento memento = entity.new HomeworkMemento(entity);
        memento.saveToMemento();
    }

    @Override
    public void undoUpdate(Homework entity, CRUDRepository<Homework> dao) {
        Homework.HomeworkMemento memento = entity.new HomeworkMemento(entity);
        int size = memento.getMementoSize();
        if (!isDeleted(entity, dao)) {
            if (size > 1) {
                Homework.HomeworkMemento beforeState = entity.getMementos().get(size - 2);
                Homework homework = HomeworkMemento.fromMemento(beforeState);
                dao.update(homework);
                memento.undo();
            } else {
                System.out.println("Це початковий стан. Попереднього стану об'єкта не існує!");
            }
        } else{
            System.out.println("Об'єкт видалений. Повернення неможливе!");
        }
    }

    private boolean isDeleted(Homework entity, CRUDRepository<Homework> dao) {
        return dao.findById(entity.getHomeworkId()) == null;
    }
}
