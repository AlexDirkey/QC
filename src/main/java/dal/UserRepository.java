package dal;
import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;

public class UserRepository {

    private final String url = "jdbc:sqlite:belsign.db";

    public User FindByUsername(String username) {

        try (Connection conn = DriverManager.getConnection(url)) {

            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())  {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );

            }
        } catch (Exception e) {
            e.printStackTrace();
        } return null;
    }
}
