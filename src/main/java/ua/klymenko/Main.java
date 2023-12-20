package ua.klymenko;


import ua.klymenko.DAO.EntityDAO.HomeworkDAO;
import ua.klymenko.Memento.HomeworkCaretaker;
import ua.klymenko.Observer.Publisher;
import ua.klymenko.DAO.Factory.DAOFactory;
import ua.klymenko.DAO.Factory.DAOFactoryImpl;
import ua.klymenko.entity.Homework;

public class Main {
    public static void main(String[] args) {
        Publisher publisher = new Publisher();

        DAOFactory factory = new DAOFactoryImpl();
        HomeworkDAO homeworkDAO = factory.getHomeworkDAO(publisher);


        Homework homework = new Homework.Builder()
                .setHomeworkId("1")
                .setDescription("TEST")
                .setDueDateTime(java.time.LocalDateTime.now().plusHours(1))
                .build();
        HomeworkCaretaker homeworkCaretaker = new HomeworkCaretaker();
        homeworkCaretaker.add(homework, homeworkDAO);

        System.out.println("Initial state");
        System.out.println(homeworkDAO.findAll());

        System.out.println("Undo try");
        homeworkCaretaker.undoUpdate(homework, homeworkDAO);

        homework.setDescription("Вивчити відповіді");
        homeworkCaretaker.update(homework, homeworkDAO);
        System.out.println("First update");
        System.out.println(homeworkDAO.findAll());

        homework.setDescription("Прчитати сторінки 1-10");
        homeworkCaretaker.update(homework, homeworkDAO);
        System.out.println("Second update");
        System.out.println(homeworkDAO.findAll());

        homeworkCaretaker.undoUpdate(homework, homeworkDAO);
        System.out.println("First undo");
        System.out.println(homeworkDAO.findAll());

        homeworkCaretaker.undoUpdate(homework, homeworkDAO);
        System.out.println("Second undo");
        System.out.println(homeworkDAO.findAll());

        homeworkDAO.delete(homework.getHomeworkId());
        System.out.println("Undo after delete");
        homeworkCaretaker.undoUpdate(homework, homeworkDAO);
    }
}