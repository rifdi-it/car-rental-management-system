package carrentalsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Change these to your DB credentials
    private static final String URL = "jdbc:mysql://localhost:3306/carrental?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = ""; // set your DB password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
