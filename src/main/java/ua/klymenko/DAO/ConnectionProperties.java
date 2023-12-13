package ua.klymenko.DAO;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConnectionProperties {
    private String url;
    private String user;
    private String password;

    public ConnectionProperties() {
        loadProperties();
    }

    private void loadProperties() {
        Properties config = new Properties();
        try {
            config.load(new FileInputStream("application.properties"));
            url = config.getProperty("database.url");
            user = config.getProperty("database.user");
            password = config.getProperty("database.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "ConnectionProperties{" +
                "url='" + url + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
