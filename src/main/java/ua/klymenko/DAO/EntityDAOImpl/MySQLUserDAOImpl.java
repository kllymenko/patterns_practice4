package ua.klymenko.DAO.EntityDAOImpl;


import ua.klymenko.DAO.EntityDAO.UserDAO;
import ua.klymenko.Observer.Publisher;
import ua.klymenko.entity.Homework;
import ua.klymenko.entity.Lesson;
import ua.klymenko.entity.User;
import ua.klymenko.entity.enums.Role;
import ua.klymenko.entity.enums.Sex;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MySQLUserDAOImpl implements UserDAO {

    private static final String INSERT_USER = "INSERT INTO school.user (name, surname, phone, email, password, sex, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_LESSON = "INSERT INTO school.lesson (name, topic, homework_id, date, time_start, time_end, cab_num) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_SCHEDULE = "INSERT INTO school.schedule (user_id, lesson_id, grade) VALUES (?, ?, ?)";
    private static final String INSERT_HOMEWORK = "INSERT INTO school.homework (description, due_datetime) VALUES (?, ?)";
    private static final String SELECT_USERS = "SELECT * FROM user";
    private static final String SELECT_USER = "SELECT * FROM user WHERE user_id = ?";
    private static final String SELECT_SCHEDULE = "SELECT * FROM schedule s JOIN lesson l ON s.lesson_id = l.lesson_id JOIN homework h ON l.homework_id = h.homework_id WHERE s.user_id = ?";
    private static final String UPDATE = "UPDATE school.user SET name=?, surname=?, phone=?, email = ?, password = ?, sex = ?, role = ? WHERE user_id=?";
    private static final String UPDATE_PASSWORD = "UPDATE school.user SET password=? WHERE user_id=?";
    private static final String DELETE = "CALL DeleteUserAndSchedule(?)";

    private final Connection con;
    private final Publisher publisher;

    public MySQLUserDAOImpl(Connection connection, Publisher publisher) {
        con = connection;
        this.publisher = publisher;
    }

    @Override
    public User findById(String id) {
        User user = new User();
        try (PreparedStatement ps = con.prepareStatement(SELECT_USER)) {
            ps.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = mapUser(rs);
                    try (PreparedStatement psSchedule = con.prepareStatement(SELECT_SCHEDULE)) {
                        psSchedule.setInt(1, Integer.parseInt(id));
                        try (ResultSet rsSchedule = psSchedule.executeQuery()) {
                            while (rsSchedule.next()) {
                                int grade = rsSchedule.getInt("grade");
                                Lesson lesson = mapLesson(rsSchedule);
                                Homework homework = mapHomework(rsSchedule);
                                lesson.setHomework(homework);
                                user.setLessonsWithGrade(lesson, grade);
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return user;
    }


    public String insert(User entity) {
        try {
            con.setAutoCommit(false);
            try (PreparedStatement ps1 = con.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
                int k = 0;
                ps1.setString(++k, entity.getName());
                ps1.setString(++k, entity.getSurname());
                ps1.setString(++k, entity.getPhone());
                ps1.setString(++k, entity.getEmail());
                ps1.setString(++k, entity.getPassword());
                ps1.setString(++k, entity.getSex().toString());
                ps1.setString(++k, entity.getRole().toString());
                ps1.executeUpdate();
                ResultSet generatedKeys1 = ps1.getGeneratedKeys();
                if (generatedKeys1.next()) {
                    entity.setUserId(String.valueOf(generatedKeys1.getInt(1)));
                    try (PreparedStatement ps2 = con.prepareStatement(INSERT_LESSON, Statement.RETURN_GENERATED_KEYS)) {
                        for (Map.Entry<Lesson, Integer> entry : entity.getLessonsWithGrade().entrySet()) {
                            Lesson lesson = entry.getKey();
                            int grade = entry.getValue();
                            try (PreparedStatement psHomework = con.prepareStatement(INSERT_HOMEWORK, Statement.RETURN_GENERATED_KEYS)) {
                                k = 0;
                                psHomework.setString(++k, lesson.getHomework().getDescription());
                                psHomework.setTimestamp(++k, Timestamp.valueOf(lesson.getHomework().getDueDateTime()));
                                psHomework.executeUpdate();

                                ResultSet generatedKeysHomework = psHomework.getGeneratedKeys();
                                if (generatedKeysHomework.next()) {
                                    lesson.getHomework().setHomeworkId(String.valueOf(generatedKeysHomework.getInt(1)));
                                }
                            }
                            k = 0;
                            ps2.setString(++k, lesson.getName());
                            ps2.setString(++k, lesson.getTopic());
                            ps2.setInt(++k, Integer.parseInt(lesson.getHomework().getHomeworkId()));
                            ps2.setDate(++k, Date.valueOf(lesson.getDate()));
                            ps2.setTime(++k, Time.valueOf(lesson.getTimeStart()));
                            ps2.setTime(++k, Time.valueOf(lesson.getTimeEnd()));
                            ps2.setInt(++k, lesson.getCabNum());
                            ps2.executeUpdate();
                            ResultSet generatedKeys2 = ps2.getGeneratedKeys();
                            if (generatedKeys2.next()) {
                                int lessonId = generatedKeys2.getInt(1);

                                try (PreparedStatement ps3 = con.prepareStatement(INSERT_SCHEDULE)) {
                                    k = 0;
                                    ps3.setInt(++k, Integer.parseInt(entity.getUserId()));
                                    ps3.setInt(++k, lessonId);
                                    ps3.setInt(++k, grade);
                                    ps3.executeUpdate();
                                }
                            }
                        }
                    }
                    con.commit();
                    publisher.notifyAdd("UserAdd", entity);
                }
            } catch (SQLException ex) {
                try {
                    con.rollback();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                throw new RuntimeException("Error inserting user", ex);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return entity.getUserId();
    }


    @Override
    public boolean update(User entity) {
        try (PreparedStatement ps = con.prepareStatement(UPDATE)) {
            int k = 0;
            ps.setString(++k, entity.getName());
            ps.setString(++k, entity.getSurname());
            ps.setString(++k, entity.getPhone());
            ps.setString(++k, entity.getEmail());
            ps.setString(++k, entity.getPassword());
            ps.setString(++k, String.valueOf(entity.getSex()));
            ps.setString(++k, String.valueOf(entity.getRole()));
            ps.setInt(++k, Integer.parseInt(entity.getUserId()));
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                publisher.notifyUpdate("UserUpdate", entity);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(String id) {
        User deletedUser = findById(id);
        try (PreparedStatement ps = con.prepareStatement(DELETE)) {
            ps.setInt(1, Integer.parseInt(id));
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0 && deletedUser != null) {
                publisher.notifyRemove("UserRemove", deletedUser);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SELECT_USERS)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = mapUser(rs);
                    try (PreparedStatement psSchedule = con.prepareStatement(SELECT_SCHEDULE)) {
                        psSchedule.setInt(1, Integer.parseInt(user.getUserId()));
                        try (ResultSet rsSchedule = psSchedule.executeQuery()) {
                            while (rsSchedule.next()) {
                                int grade = rsSchedule.getInt("grade");
                                Lesson lesson = mapLesson(rsSchedule);
                                Homework homework = mapHomework(rsSchedule);
                                lesson.setHomework(homework);
                                user.setLessonsWithGrade(lesson, grade);
                            }
                            userList.add(user);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return userList;
    }


    @Override
    public boolean updatePassword(User entity, String newPassword) {
        try (PreparedStatement ps = con.prepareStatement(UPDATE_PASSWORD)) {
            int k = 0;
            ps.setString(++k, newPassword);
            ps.setInt(++k, Integer.parseInt(entity.getUserId()));
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                publisher.notifyUpdate("UserUpdate", entity);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        return new User.Builder()
                .setUserId(String.valueOf(rs.getInt("user_id")))
                .setName(rs.getString("name"))
                .setSurname(rs.getString("surname"))
                .setPhone(rs.getString("phone"))
                .setEmail(rs.getString("email"))
                .setPassword(rs.getString("password"))
                .setSex(Sex.valueOf(rs.getString("sex").toUpperCase()))
                .setRole(Role.valueOf(rs.getString("role").toUpperCase()))
                .build();

    }

    private Lesson mapLesson(ResultSet rs) throws SQLException {
        return new Lesson.Builder()
                .setLessonId(String.valueOf(rs.getInt("lesson_id")))
                .setName(rs.getString("name"))
                .setTopic(rs.getString("topic"))
                .setDate(rs.getDate("date").toLocalDate())
                .setTimeStart(rs.getTime("time_start").toLocalTime())
                .setTimeEnd(rs.getTime("time_end").toLocalTime())
                .setCabNum(rs.getInt("cab_num"))
                .build();
    }

    private Homework mapHomework(ResultSet rs) throws SQLException {
        return new Homework.Builder()
                .setHomeworkId(String.valueOf(rs.getInt("homework_id")))
                .setDescription(rs.getString("description"))
                .setDueDateTime(rs.getTimestamp("due_datetime").toLocalDateTime())
                .build();
    }
}
