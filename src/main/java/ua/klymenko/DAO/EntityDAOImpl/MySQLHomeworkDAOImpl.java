package ua.klymenko.DAO.EntityDAOImpl;

import ua.klymenko.DAO.EntityDAO.HomeworkDAO;
import ua.klymenko.Observer.Publisher;
import ua.klymenko.entity.Homework;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLHomeworkDAOImpl implements HomeworkDAO {

    private static final String INSERT_HOMEWORK = "INSERT INTO school.homework (description, due_datetime) VALUES (?, ?)";
    private static final String GET_HOMEWORK = "SELECT * from school.homework WHERE homework_id=?";
    private static final String UPDATE = "UPDATE homework SET description = ?, due_datetime = ? WHERE homework_id = ?";
    private static final String DELETE = "DELETE FROM school.homework WHERE homework_id = ?";
    private static final String GET_ALL_HOMEWORKS = "SELECT * FROM school.homework";
    private static final String UPDATE_DUE_DATE_TIME = "UPDATE school.homework SET due_datetime=? WHERE homework_id=?";
    private final Connection con;
    private final Publisher publisher;

    public MySQLHomeworkDAOImpl(Connection connection, Publisher publisher) {
        con = connection;
        this.publisher = publisher;
    }


    @Override
    public String insert(Homework entity) {
        try (PreparedStatement ps = con.prepareStatement(INSERT_HOMEWORK, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getDescription());
            ps.setTimestamp(2, Timestamp.valueOf(entity.getDueDateTime()));

            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setHomeworkId(String.valueOf(generatedKeys.getInt(1)));
            }
            publisher.notifyAdd("HomeworkAdd", entity);
        } catch (SQLException ex) {
            throw new RuntimeException("Error inserting Homework", ex);
        }
        return entity.getHomeworkId();
    }

    @Override
    public boolean update(Homework entity) {
        try (PreparedStatement ps = con.prepareStatement(UPDATE)) {
            ps.setString(1, entity.getDescription());
            ps.setTimestamp(2, Timestamp.valueOf(entity.getDueDateTime()));
            ps.setInt(3, Integer.parseInt(entity.getHomeworkId()));

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                publisher.notifyUpdate("HomeworkUpdate", entity);
            }

            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating homework", e);
        }
    }

    @Override
    public boolean delete(String id) {
        Homework deletedHomework = findById(id);
        try (PreparedStatement ps = con.prepareStatement(DELETE)) {
            ps.setInt(1, Integer.parseInt(id));

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0 && deletedHomework != null) {
                publisher.notifyRemove("HomeworkRemove", deletedHomework);
            }

            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting homework", e);

        }
    }

    @Override
    public List<Homework> findAll() {
        List<Homework> homeworkList = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(GET_ALL_HOMEWORKS)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Homework homework = mapHomework(rs);
                    homeworkList.add(homework);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all homework", e);
        }

        return homeworkList;
    }

    @Override
    public Homework findById(String id) {
        try (PreparedStatement ps = con.prepareStatement(GET_HOMEWORK)) {
            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapHomework(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving homework by ID", e);
        }
        return null;
    }


    @Override
    public boolean updateDueDateTime(Homework entity, Timestamp dueDateTime) {
        try (PreparedStatement ps = con.prepareStatement(UPDATE_DUE_DATE_TIME)) {
            ps.setTimestamp(1, dueDateTime);
            ps.setInt(2, Integer.parseInt(entity.getHomeworkId()));

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                publisher.notifyUpdate("HomeworkUpdate", entity);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating due date and time for homework", e);
        }
    }


    private Homework mapHomework(ResultSet rs) throws SQLException {
        return new Homework.Builder()
                .setHomeworkId(String.valueOf(rs.getInt("homework_id")))
                .setDescription(rs.getString("description"))
                .setDueDateTime(rs.getTimestamp("due_datetime").toLocalDateTime())
                .build();
    }
}
