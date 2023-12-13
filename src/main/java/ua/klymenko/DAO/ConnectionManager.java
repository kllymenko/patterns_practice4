package ua.klymenko.DAO;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class ConnectionManager {
    private static ConnectionManager instance;
    private Connection connection;
    private ConnectionProperties properties;

    private ConnectionManager(ConnectionProperties properties) {
        this.properties = properties;
    }

    public static ConnectionManager getInstance(ConnectionProperties properties) {
        if (instance == null) {
            instance = new ConnectionManager(properties);
            instance.initializeConnection();
        }
        return instance;
    }

    private void initializeConnection() {
        try {
            connection = DriverManager.getConnection(properties.getUrl(), properties.getUser(), properties.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static void rollback(Connection connection) {
        try {
            Objects.requireNonNull(connection).rollback();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void close(AutoCloseable... closeable) {
        for (AutoCloseable c : closeable) {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

