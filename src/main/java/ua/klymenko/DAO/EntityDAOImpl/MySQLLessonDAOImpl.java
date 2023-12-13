package ua.klymenko.DAO.EntityDAOImpl;


import ua.klymenko.DAO.EntityDAO.LessonDAO;
import ua.klymenko.Observer.Publisher;
import ua.klymenko.entity.Homework;
import ua.klymenko.entity.Lesson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLLessonDAOImpl implements LessonDAO {

    public static final String INSERT_HOMEWORK = "INSERT INTO homework (description, due_datetime) VALUES (?, ?)";
    public static final String INSERT_LESSON = "INSERT INTO lesson (name, date, time_start, time_end, cab_num, topic, homework_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String SELECT_LESSON = "SELECT * FROM lesson JOIN homework on lesson.homework_id = homework.homework_id WHERE lesson_id = ?";
    public static final String SELECT_LESSONS = "SELECT * FROM lesson JOIN homework on lesson.homework_id = homework.homework_id";
    public static final String UPDATE_LESSON_NAME = "UPDATE school.lesson SET name = ? WHERE lesson_id = ?";
    public static final String DELETE_LESSON = "CALL DeleteLessonAndSchedule(?)";
    public static final String UPDATE_HOMEWORK = "UPDATE school.homework SET description = ?, due_datetime = ? WHERE homework_id = ?;";
    public static final String UPDATE_LESSON = "UPDATE school.lesson SET name = ?, date = ?, time_start = ?, time_end = ?, cab_num = ?, topic = ? WHERE lesson_id = ?;";

    private final Connection con;
    private final Publisher publisher;

    public MySQLLessonDAOImpl(Connection connection, Publisher publisher) {
        con = connection;
        this.publisher = publisher;
    }


    @Override
    public String insert(Lesson entity) {
        try {
            con.setAutoCommit(false);
            Homework homework = entity.getHomework();
            try (PreparedStatement ps1 = con.prepareStatement(INSERT_HOMEWORK, Statement.RETURN_GENERATED_KEYS)) {
                ps1.setString(1, homework.getDescription());
                ps1.setTimestamp(2, Timestamp.valueOf(homework.getDueDateTime()));
                ps1.executeUpdate();
                ResultSet generatedKeys = ps1.getGeneratedKeys();
                if (generatedKeys.next()) {
                    homework.setHomeworkId(String.valueOf(generatedKeys.getInt(1)));
                    try (PreparedStatement psLesson = con.prepareStatement(INSERT_LESSON, Statement.RETURN_GENERATED_KEYS)) {
                        psLesson.setString(1, entity.getName());
                        psLesson.setDate(2, Date.valueOf(entity.getDate()));
                        psLesson.setTime(3, Time.valueOf(entity.getTimeStart()));
                        psLesson.setTime(4, Time.valueOf(entity.getTimeEnd()));
                        psLesson.setInt(5, entity.getCabNum());
                        psLesson.setString(6, entity.getTopic());
                        psLesson.setInt(7, Integer.parseInt(homework.getHomeworkId()));
                        psLesson.executeUpdate();
                        ResultSet generatedKeysLesson = psLesson.getGeneratedKeys();
                        if (generatedKeysLesson.next()) {
                            entity.setLessonId(String.valueOf(generatedKeysLesson.getInt(1)));
                        }
                    }
                }
            }
            con.commit();
            publisher.notifyAdd("LessonAdd", entity);
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                throw new RuntimeException("Error rolling back transaction", e);
            }
            ex.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Error setting auto commit to true", e);
            }
        }
        return entity.getLessonId();
    }

    @Override
    public boolean update(Lesson entity) {
        try {
            con.setAutoCommit(false);
            Homework homework = entity.getHomework();
            try (PreparedStatement psUpdateHomework = con.prepareStatement(UPDATE_HOMEWORK)) {
                psUpdateHomework.setString(1, homework.getDescription());
                psUpdateHomework.setTimestamp(2, Timestamp.valueOf(homework.getDueDateTime()));
                psUpdateHomework.setInt(3, Integer.parseInt(homework.getHomeworkId()));
                int updatedHomeworkRows = psUpdateHomework.executeUpdate();

                if (updatedHomeworkRows > 0) {
                    try (PreparedStatement psUpdateLesson = con.prepareStatement(UPDATE_LESSON)) {
                        psUpdateLesson.setString(1, entity.getName());
                        psUpdateLesson.setDate(2, Date.valueOf(entity.getDate()));
                        psUpdateLesson.setTime(3, Time.valueOf(entity.getTimeStart()));
                        psUpdateLesson.setTime(4, Time.valueOf(entity.getTimeEnd()));
                        psUpdateLesson.setInt(5, entity.getCabNum());
                        psUpdateLesson.setString(6, entity.getTopic());
                        psUpdateLesson.setInt(7, Integer.parseInt(entity.getLessonId()));
                        int updatedLessonRows = psUpdateLesson.executeUpdate();

                        con.commit();
                        if (updatedLessonRows > 0) {
                            publisher.notifyUpdate("LessonUpdate", entity);
                        }
                        return updatedLessonRows > 0;
                    }
                }
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                throw new RuntimeException("Error rolling back transaction", e);
            }
            ex.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Error setting auto commit to true", e);
            }
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        Lesson deletedLesson = findById(id);
        try (PreparedStatement psDeleteLesson = con.prepareStatement(DELETE_LESSON)) {
            psDeleteLesson.setInt(1, Integer.parseInt(id));
            int deletedLessonRows = psDeleteLesson.executeUpdate();

            if (deletedLessonRows > 0 && deletedLesson != null) {
                publisher.notifyRemove("LessonRemove", deletedLesson);
            }
            return deletedLessonRows > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Lesson> findAll() {
        List<Lesson> lessons = new ArrayList<>();
        try (PreparedStatement psAllLessons = con.prepareStatement(SELECT_LESSONS)) {
            try (ResultSet rsAllLessons = psAllLessons.executeQuery()) {
                while (rsAllLessons.next()) {
                    Lesson lesson = mapLesson(rsAllLessons);
                    lesson.setHomework(mapHomework(rsAllLessons));
                    lessons.add(lesson);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lessons;
    }

    @Override
    public Lesson findById(String id) {
        Lesson lesson = new Lesson();
        try (PreparedStatement psLesson = con.prepareStatement(SELECT_LESSON)) {
            psLesson.setInt(1, Integer.parseInt(id));
            try (ResultSet rsLesson = psLesson.executeQuery()) {
                if (rsLesson.next()) {
                    lesson = mapLesson(rsLesson);
                    lesson.setHomework(mapHomework(rsLesson));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lesson;
    }


    @Override
    public boolean updateName(Lesson entity, String newName) {
        try (PreparedStatement psUpdateLessonName = con.prepareStatement(UPDATE_LESSON_NAME)) {
            psUpdateLessonName.setString(1, newName);
            psUpdateLessonName.setInt(2, Integer.parseInt(entity.getLessonId()));
            int updatedLessonRows = psUpdateLessonName.executeUpdate();
            if (updatedLessonRows > 0) {
                publisher.notifyUpdate("LessonUpdate", entity);
            }
            return updatedLessonRows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
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
