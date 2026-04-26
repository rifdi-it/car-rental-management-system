package carrentalsystem.dao;

import carrentalsystem.models.User;
import carrentalsystem.util.DBConnection;
import carrentalsystem.util.PasswordUtil;

import java.sql.*;

public class UserDAO {
    public static boolean register(User user) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getPassword());
            pst.setString(3, user.getRole());
            return pst.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public static User authenticate(String username, String password) {
        String sql = "SELECT id, username, password, role FROM users WHERE username = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String hashed = rs.getString("password");
                    if (PasswordUtil.check(password, hashed)) {
                        User u = new User();
                        u.setId(rs.getInt("id"));
                        u.setUsername(rs.getString("username"));
                        u.setPassword(hashed);
                        u.setRole(rs.getString("role"));
                        return u;
                    }
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return null;
    }
}
