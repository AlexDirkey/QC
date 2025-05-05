package dal;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserRepository håndterer database-operationer for User.
 */
public class UserRepository {

    private DataBaseConnector DatabaseConnector;

    /**
     * Finder en bruger baseret på brugernavn.
     * @param username Brugernavnet, der søges på.
     * @return User objekt, hvis fundet; ellers null.
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String uname = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");

                return new User(id, uname, password, role);
            }

        } catch (SQLException e) {
            System.err.println("Error in findByUsername: " + e.getMessage());
        }
        return null;
    }
}

