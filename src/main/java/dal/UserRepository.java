package dal;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepository {

    private final DataBaseConnector databaseConnector = new DataBaseConnector();


    //Finder en bruger ud fra brugernavnet

    public User findByUsername(String username) {

        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultToUser(rs);
            }
        } catch (SQLException e) {

            System.err.println("Error in findByUsername: " + e.getMessage());
        }
        return null;
    }

    //Gemmer en ny bruger

    public void save(User user) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error in save: " + e.getMessage());
        }
    }
    //Sletter en bruger

    public void deleteByUsername(String username) {

        String sql = "DELETE FROM users WHERE username = ?";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {

            System.err.println("Error in deleteByUsername: " + e.getMessage());
        }
    }
    //Finder brugere fra databasen, under tabellen 'users'

    public List<User> findAll() {

        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            //Looper igennem resultaterne
            while (rs.next()) {
                users.add(mapResultToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error in findAll: " + e.getMessage());
        }
        return users;
    }

    //Henter alle brugernavne

    public List<String> getAllUsernames() {

        return findAll().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }
    //Opdaterer rolle og password for en eksisterende bruger

    public void updateUser(User user) {

        String sql = "UPDATE users SET password = ?, role = ? WHERE username = ?";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getRole());
            stmt.setString(3, user.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error in updateUser: " + e.getMessage());
        }
    }

    //Mapper result til user
    private User mapResultToUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String uname = rs.getString("username");
        String password = rs.getString("password");
        String role = rs.getString("role");
        return new User(id, uname, password, role);
    }
}
