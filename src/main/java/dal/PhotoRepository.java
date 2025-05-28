package dal;

import model.Photo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PhotoRepository {
    private final DataBaseConnector databaseConnector = new DataBaseConnector();

    public void save(Photo photo) {
        String sql = "INSERT INTO photos (order_number, file_path, uploaded_by, uploaded_at, comment, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, photo.getOrderNumber());
            stmt.setString(2, photo.getFilePath());
            stmt.setString(3, photo.getUploadedBy());
            stmt.setTimestamp(4, Timestamp.valueOf(photo.getUploadedAt()));
            stmt.setString(5, photo.getComment());
            stmt.setString(6, photo.getStatus());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving photo: " + e.getMessage());
        }
    }

    public List<Photo> findUnapprovedPhotos() {
        List<Photo> result = new ArrayList<>();
        String sql = "SELECT * FROM photos WHERE status IS NULL";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.add(mapResultSetToPhoto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error in findUnapprovedPhotos: " + e.getMessage());
        }

        return result;
    }

    public void updateStatus(int photoId, String status) {
        String sql = "UPDATE photos SET status = ? WHERE id = ?";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, photoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating photo status: " + e.getMessage());
        }
    }

    public List<Photo> findByOrderNumber(String orderNumber) {
        List<Photo> result = new ArrayList<>();
        String sql = "SELECT * FROM photos WHERE order_number = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderNumber);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(mapResultSetToPhoto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error in findByOrderNumber: " + e.getMessage());
        }

        return result;
    }

    private Photo mapResultSetToPhoto(ResultSet rs) throws SQLException {
        return new Photo(
                rs.getInt("id"),
                rs.getString("order_number"),
                rs.getString("file_path"),
                rs.getString("uploaded_by"),
                rs.getTimestamp("uploaded_at").toLocalDateTime(),
                rs.getString("comment"),
                rs.getString("status")
        );
    }
}

