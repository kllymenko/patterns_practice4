package ua.klymenko;


import ua.klymenko.DAO.EntityDAO.HomeworkDAO;
import ua.klymenko.DAO.EntityDAO.LessonDAO;
import ua.klymenko.DAO.EntityDAO.UserDAO;
import ua.klymenko.Observer.Publisher;
import ua.klymenko.Observer.Notifiers.HomeworkNotifier;
import ua.klymenko.Observer.Notifiers.LessonNotifier;
import ua.klymenko.Observer.Notifiers.UserNotifier;
import ua.klymenko.DAO.Factory.DAOFactory;
import ua.klymenko.DAO.Factory.DAOFactoryImpl;
import ua.klymenko.entity.Homework;
import ua.klymenko.entity.Lesson;
import ua.klymenko.entity.User;
import ua.klymenko.entity.enums.Role;
import ua.klymenko.entity.enums.Sex;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Publisher publisher = new Publisher();

        DAOFactory factory = new DAOFactoryImpl();
        UserDAO userDAO = factory.getUserDAO(publisher);
        LessonDAO lessonDAO = factory.getLessonDAO(publisher);
        HomeworkDAO homeworkDAO = factory.getHomeworkDAO(publisher);

        HomeworkNotifier homeworkNotifier = new HomeworkNotifier();
        publisher.subscribeToAll("Homework", homeworkNotifier);

        LessonNotifier lessonNotifier = new LessonNotifier();
        publisher.subscribeToAll("Lesson", lessonNotifier);

        UserNotifier userNotifier = new UserNotifier();
        publisher.subscribeToAll("User", userNotifier);

        test(userDAO, lessonDAO, homeworkDAO);

        publisher.unsubscribe("LessonRemove", lessonNotifier);
        publisher.unsubscribe("HomeworkRemove", homeworkNotifier);

        test(userDAO, lessonDAO, homeworkDAO);
    }

    public static void writeUser(User user) {
        // Виведення інформації у консоль
        System.out.println("User 1:");
        System.out.println("    Name: " + user.getName());
        System.out.println("    Surname: " + user.getSurname());
        System.out.println("    Phone: " + user.getPhone());
        System.out.println("    Email: " + user.getEmail());
        System.out.println("    Sex: " + user.getSex());
        System.out.println("    Role: " + user.getRole());
        System.out.println("    Password: " + user.getPassword());
        System.out.println("\n    Lessons with Grades:");

        for (Map.Entry<Lesson, Integer> entry : user.getLessonsWithGrade().entrySet()) {
            Lesson lesson = entry.getKey();
            int grade = entry.getValue();

            System.out.println("        " + lesson.getName() + ":");
            System.out.println("            Name: " + lesson.getName());
            System.out.println("            Topic: " + lesson.getTopic());
            System.out.println("            Date: " + lesson.getDate());
            System.out.println("            Time Start: " + lesson.getTimeStart());
            System.out.println("            Time End: " + lesson.getTimeEnd());
            System.out.println("            CabNum: " + lesson.getCabNum());
            System.out.println("            Homework:");
            System.out.println("                Description: " + lesson.getHomework().getDescription());
            System.out.println("                Due Date Time: " + lesson.getHomework().getDueDateTime());
            System.out.println("            Grade: " + grade);
        }
    }

    public static void test(UserDAO userDAO, LessonDAO lessonDAO, HomeworkDAO homeworkDAO){
        User user = new User.Builder()
                .setName("John")
                .setSurname("Doe")
                .setPhone("123456789")
                .setEmail("john.doe@example.com")
                .setPassword("password")
                .setSex(Sex.MALE)
                .setRole(Role.STUDENT)
                .build();
        for (int i = 1; i <= 5; i++) {
            Lesson lesson = new Lesson.Builder()
                    .setName("Lesson " + i)
                    .setTopic("Topic " + i)
                    .setDate(java.time.LocalDate.now())
                    .setTimeStart(java.time.LocalTime.now())
                    .setTimeEnd(java.time.LocalTime.now().plusHours(1))
                    .setCabNum(i)
                    .setHomework(new Homework.Builder()
                            .setDescription("Homework for Lesson " + i)
                            .setDueDateTime(java.time.LocalDateTime.now().plusDays(i))
                            .build())
                    .build();

            user.setLessonsWithGrade(lesson, i * 10);
        }

        // Insert the User into the database
        String userId = userDAO.insert(user);

        // Test findById
        User fetchedUser = userDAO.findById(userId);
        System.out.println("Fetched User by ID: " + fetchedUser);

        // Test update
        fetchedUser.setName("UpdatedName");
        boolean isUpdated = userDAO.update(fetchedUser);
        System.out.println("Is User Updated: " + isUpdated);

        // Test findAll
        List<User> userList = userDAO.findAll();
        System.out.println("All Users:");
        for (User u : userList) {
            writeUser(u);
        }

        // Test updatePassword
        String newPassword = "newPassword";
        boolean isPasswordUpdated = userDAO.updatePassword(fetchedUser, newPassword);
        System.out.println("Is Password Updated: " + isPasswordUpdated);

        // Test delete
        boolean isDeleted = userDAO.delete(userId);
        System.out.println("Is User Deleted: " + isDeleted);

        // Test findAll after deletion
        List<User> updatedUserList = userDAO.findAll();
        System.out.println("All Users after Deletion:");
        for (User u : updatedUserList) {
            System.out.println(u);
        }

        List<Lesson> lessons = lessonDAO.findAll();
        for (Lesson l : lessons) {
            boolean res = lessonDAO.delete(l.getLessonId());
            System.out.println("DELETION LESSON WITH ID =" + l.getLessonId() + ":" + res);
        }

        List<Homework> homeworks = homeworkDAO.findAll();
        for (Homework h : homeworks) {
            boolean res = homeworkDAO.delete(h.getHomeworkId());
            System.out.println("DELETION HOMEWORK WITH ID =" + h.getHomeworkId() + ":" + res);
        }

        // Test LessonDAO
        Lesson lesson = new Lesson.Builder()
                .setName("New Lesson")
                .setDate(java.time.LocalDate.now())
                .setTimeStart(java.time.LocalTime.now())
                .setTimeEnd(java.time.LocalTime.now().plusHours(1))
                .setTopic("Circle")
                .setCabNum(10)
                .setHomework(new Homework.Builder()
                        .setDescription("Homework for New Lesson")
                        .setDueDateTime(java.time.LocalDateTime.now().plusDays(1))
                        .build())
                .build();

        // Insert the Lesson into the database
        String lessonId = lessonDAO.insert(lesson);
        System.out.println("Inserted Lesson with ID: " + lessonId);

        // Test findById for Lesson
        Lesson fetchedLesson = lessonDAO.findById(lessonId);
        System.out.println("Fetched Lesson by ID: " + fetchedLesson);

        // Test updateName for Lesson
        String newLessonName = "Updated Lesson Name";
        boolean isLessonNameUpdated = lessonDAO.updateName(fetchedLesson, newLessonName);
        System.out.println("Is Lesson Name Updated: " + isLessonNameUpdated);

        // Test delete for Lesson
        boolean isLessonDeleted = lessonDAO.delete(lessonId);
        System.out.println("Is Lesson Deleted: " + isLessonDeleted);

        // Test findAll for Lesson
        List<Lesson> lessonList = lessonDAO.findAll();
        System.out.println("All Lessons:");
        for (Lesson l : lessonList) {
            System.out.println(l);
        }

        homeworks = homeworkDAO.findAll();
        for (Homework h : homeworks) {
            boolean res = homeworkDAO.delete(h.getHomeworkId());
            System.out.println("DELETION HOMEWORK WITH ID =" + h.getHomeworkId() + ":" + res);
        }

        // Test HomeworkDAO
        Homework homework = new Homework.Builder()
                .setDescription("New Homework")
                .setDueDateTime(java.time.LocalDateTime.now().plusDays(2))
                .build();

        // Insert the Homework into the database
        String homeworkId = homeworkDAO.insert(homework);
        System.out.println("Inserted Homework with ID: " + homeworkId);

        // Test findById for Homework
        Homework fetchedHomework = homeworkDAO.findById(homeworkId);
        System.out.println("Fetched Homework by ID: " + fetchedHomework);

        // Test update for Homework
        fetchedHomework.setDescription("Updated Homework");
        boolean isHomeworkUpdated = homeworkDAO.update(fetchedHomework);
        System.out.println("Is Homework Updated: " + isHomeworkUpdated);

        // Test delete for Homework
        boolean isHomeworkDeleted = homeworkDAO.delete(homeworkId);
        System.out.println("Is Homework Deleted: " + isHomeworkDeleted);

        // Test findAll for Homework
        List<Homework> homeworkList = homeworkDAO.findAll();
        System.out.println("All Homeworks:");
        for (Homework h : homeworkList) {
            System.out.println(h);
        }
    }
}