package ua.klymenko;

import ua.klymenko.Observer.Publisher;
import ua.klymenko.DAO.Factory.DAOFactory;
import ua.klymenko.DAO.Factory.DAOFactoryImpl;
import ua.klymenko.Proxy.HomeworkDAOProxy;
import ua.klymenko.Proxy.LessonDAOProxy;
import ua.klymenko.Proxy.UserDAOProxy;
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
        Role userRole = Role.STUDENT;
        DAOFactory factory = new DAOFactoryImpl();

        UserDAOProxy userDAOProxy = new UserDAOProxy(factory.getUserDAO(publisher), userRole);
        LessonDAOProxy lessonDAOProxy = new LessonDAOProxy(factory.getLessonDAO(publisher), userRole);
        HomeworkDAOProxy homeworkDAOProxy = new HomeworkDAOProxy(factory.getHomeworkDAO(publisher), userRole);

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
        String userId = userDAOProxy.insert(user);

        // Test findById
        User fetchedUser = userDAOProxy.findById(userId);
        System.out.println("Fetched User by ID: " + fetchedUser);

        // Test update
        fetchedUser.setName("UpdatedName");
        boolean isUpdated = userDAOProxy.update(fetchedUser);
        System.out.println("Is User Updated: " + isUpdated);

        // Test findAll
        List<User> userList = userDAOProxy.findAll();
        System.out.println("All Users:");
        for (User u : userList) {
            writeUser(u);
        }

        // Test updatePassword
        String newPassword = "newPassword";
        boolean isPasswordUpdated = userDAOProxy.updatePassword(fetchedUser, newPassword);
        System.out.println("Is Password Updated: " + isPasswordUpdated);

        // Test delete
        boolean isDeleted = userDAOProxy.delete(userId);
        System.out.println("Is User Deleted: " + isDeleted);

        // Test findAll after deletion
        List<User> updatedUserList = userDAOProxy.findAll();
        System.out.println("All Users after Deletion:");
        for (User u : updatedUserList) {
            System.out.println(u);
        }

        List<Lesson> lessons = lessonDAOProxy.findAll();
        for (Lesson l : lessons) {
            boolean res = lessonDAOProxy.delete(l.getLessonId());
            System.out.println("DELETION LESSON WITH ID =" + l.getLessonId() + ":" + res);
        }

        List<Homework> homeworks = homeworkDAOProxy.findAll();
        for (Homework h : homeworks) {
            boolean res = homeworkDAOProxy.delete(h.getHomeworkId());
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
        String lessonId = lessonDAOProxy.insert(lesson);
        System.out.println("Inserted Lesson with ID: " + lessonId);

        // Test findById for Lesson
        Lesson fetchedLesson = lessonDAOProxy.findById(lessonId);
        System.out.println("Fetched Lesson by ID: " + fetchedLesson);

        // Test updateName for Lesson
        String newLessonName = "Updated Lesson Name";
        boolean isLessonNameUpdated = lessonDAOProxy.updateName(fetchedLesson, newLessonName);
        System.out.println("Is Lesson Name Updated: " + isLessonNameUpdated);

        // Test delete for Lesson
        boolean isLessonDeleted = lessonDAOProxy.delete(lessonId);
        System.out.println("Is Lesson Deleted: " + isLessonDeleted);

        // Test findAll for Lesson
        List<Lesson> lessonList = lessonDAOProxy.findAll();
        System.out.println("All Lessons:");
        for (Lesson l : lessonList) {
            System.out.println(l);
        }

        homeworks = homeworkDAOProxy.findAll();
        for (Homework h : homeworks) {
            boolean res = homeworkDAOProxy.delete(h.getHomeworkId());
            System.out.println("DELETION HOMEWORK WITH ID =" + h.getHomeworkId() + ":" + res);
        }

        // Test HomeworkDAO
        Homework homework = new Homework.Builder()
                .setDescription("New Homework")
                .setDueDateTime(java.time.LocalDateTime.now().plusDays(2))
                .build();

        // Insert the Homework into the database
        String homeworkId = homeworkDAOProxy.insert(homework);
        System.out.println("Inserted Homework with ID: " + homeworkId);

        // Test findById for Homework
        Homework fetchedHomework = homeworkDAOProxy.findById(homeworkId);
        System.out.println("Fetched Homework by ID: " + fetchedHomework);

        // Test update for Homework
        fetchedHomework.setDescription("Updated Homework");
        boolean isHomeworkUpdated = homeworkDAOProxy.update(fetchedHomework);
        System.out.println("Is Homework Updated: " + isHomeworkUpdated);

        // Test delete for Homework
        boolean isHomeworkDeleted = homeworkDAOProxy.delete(homeworkId);
        System.out.println("Is Homework Deleted: " + isHomeworkDeleted);

        // Test findAll for Homework
        List<Homework> homeworkList = homeworkDAOProxy.findAll();
        System.out.println("All Homeworks:");
        for (Homework h : homeworkList) {
            System.out.println(h);
        }

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

}