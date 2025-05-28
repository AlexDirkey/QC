package dal;

import model.Photo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhotoRepository {

    private final DataBaseConnector databaseConnector = new DataBaseConnector();

    public List<Photo> findUnapprovedPhotos() {
        List<Photo> photos = new ArrayList<>();
        String sql = "SELECT * FROM photos WHERE status IS NULL";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                photos.add(mapResultSetToPhoto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error in findUnapprovedPhotos: " + e.getMessage());
        }
        return photos;
    }

    public void updateStatus(int photoId, boolean approved) {
        String sql = "UPDATE photos SET status = ? WHERE id = ?";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, approved ? "approved" : "rejected");
            stmt.setInt(2, photoId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error in updateStatus: " + e.getMessage());
        }
    }

    public List<Photo> findByOrderNumber(String orderNumber) {
        List<Photo> photos = new ArrayList<>();
        String sql = "SELECT * FROM photos WHERE order_number = ?";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, orderNumber);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                photos.add(mapResultSetToPhoto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error in findByOrderNumber: " + e.getMessage());
        }
        return photos;
    }

    private Photo mapResultSetToPhoto(ResultSet rs) throws SQLException {
        String status = rs.getString("status");
        boolean approved = "approved".equalsIgnoreCase(status);

        return new Photo(
                rs.getInt("id"),
                rs.getString("order_number"),
                rs.getString("uploaded_by"),
                status,
                rs.getTimestamp("uploaded_at").toLocalDateTime(),
                approved,
                rs.getString("comment")
        );
    }

    public void save(Photo photo) {
        String sql = "INSERT INTO photos (order_number, uploaded_by, status, uploaded_at, comment) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, photo.getOrderNumber());
            stmt.setString(2, photo.getUploadedBy());
            stmt.setString(3, photo.getStatus());
            stmt.setTimestamp(4, Timestamp.valueOf(photo.getUploadedAt()));
            stmt.setString(5, photo.getComment());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error in save: " + e.getMessage());
        }
    }

    public List<Photo> findApprovedPhotos() {
        List<Photo> photos = new ArrayList<>();
        String sql = "SELECT * FROM photos WHERE status = 'approved'";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                photos.add(mapResultSetToPhoto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error in findApprovedPhotos: " + e.getMessage());
        }
        return photos;
    }
}





