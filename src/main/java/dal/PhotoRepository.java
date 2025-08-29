package dal;

import model.Photo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PhotoRepository {

    private static final String STATUS_PENDING   = "PENDING";
    private static final String STATUS_IN_REVIEW = "IN_REVIEW";
    private static final String STATUS_APPROVED  = "APPROVED";
    private static final String STATUS_REJECTED  = "REJECTED";

    private Connection connect() throws SQLException {
        return DataBaseConnector.getConnection();
    }

    // INSERT ét foto
    public void save(Photo photo) {
        final String sql =
                "INSERT INTO dbo.Photos (order_number, uploaded_by, status, uploaded_at, comment, file_path) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, photo.getOrderNumber());
            ps.setString(2, photo.getUploadedBy());
            ps.setString(3, photo.getStatus() != null ? photo.getStatus() : STATUS_PENDING);
            ps.setTimestamp(4, Timestamp.valueOf(
                    photo.getUploadedAt() != null ? photo.getUploadedAt() : LocalDateTime.now()));
            ps.setString(5, photo.getComment());
            ps.setString(6, photo.getFilePath());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Kunne ikke gemme foto", e);
        }
    }

    // INSERT flere fotos i én transaktion
    public void saveAll(List<Photo> photos) throws SQLException {
        if (photos == null || photos.isEmpty()) return;

        final String sql =
                "INSERT INTO dbo.Photos (order_number, uploaded_by, status, uploaded_at, comment, file_path) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            try {
                for (Photo p : photos) {
                    ps.setString(1, p.getOrderNumber());
                    ps.setString(2, p.getUploadedBy());
                    ps.setString(3, p.getStatus() != null ? p.getStatus() : STATUS_PENDING);
                    ps.setTimestamp(4, Timestamp.valueOf(
                            p.getUploadedAt() != null ? p.getUploadedAt() : LocalDateTime.now()));
                    ps.setString(5, p.getComment());
                    ps.setString(6, p.getFilePath());
                    ps.addBatch();
                }
                ps.executeBatch();
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    // READ alle fotos
    public List<Photo> getAllPhotos() {
        final String sql = "SELECT * FROM dbo.Photos ORDER BY uploaded_at DESC";
        List<Photo> result = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) result.add(mapResultSetToPhoto(rs));
            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Kunne ikke hente alle fotos", e);
        }
    }

    // READ pr. ordrenummer
    public List<Photo> findByOrderNumber(String orderNumber) {
        final String sql = "SELECT * FROM dbo.Photos WHERE order_number = ? ORDER BY uploaded_at ASC";
        List<Photo> result = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, orderNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(mapResultSetToPhoto(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Kunne ikke hente fotos for ordre=" + orderNumber, e);
        }
    }

    // READ pr. status
    public List<Photo> findByStatus(String status) {
        final String sql = "SELECT * FROM dbo.Photos WHERE status = ? ORDER BY uploaded_at ASC";
        List<Photo> result = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(mapResultSetToPhoto(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Kunne ikke hente fotos for status=" + status, e);
        }
    }

    // UPDATE → APPROVED / REJECTED (fjernet in_review)
    public void updateStatus(int id, boolean approved) {
        final String sql =
                "UPDATE dbo.Photos SET status = ?, approved = ? WHERE id = ?";
        final String newStatus = approved ? STATUS_APPROVED : STATUS_REJECTED;

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setBoolean(2, approved);
            ps.setInt(3, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Kunne ikke opdatere photo status, id=" + id, e);
        }
    }

    // Valgfrit trin: PENDING → IN_REVIEW (hvis du vil have “send til QA”)
    public void updateStatusToInReview(int id) {
        final String sql =
                "UPDATE dbo.Photos SET status = ?, approved = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, STATUS_IN_REVIEW);
            ps.setBoolean(2, false);
            ps.setInt(3, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Kunne ikke sætte photo IN_REVIEW, id=" + id, e);
        }
    }

    private Photo mapResultSetToPhoto(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String orderNumber = rs.getString("order_number");
        String uploadedBy = rs.getString("uploaded_by");
        String status = rs.getString("status");
        Timestamp ts = rs.getTimestamp("uploaded_at");
        LocalDateTime uploadedAt = ts != null ? ts.toLocalDateTime() : null;
        String comment = rs.getString("comment");
        boolean approved = false;
        try { approved = rs.getBoolean("approved"); } catch (SQLException ignored) {}

        Photo photo = new Photo(id, orderNumber, uploadedBy, status, uploadedAt, approved, comment);
        try { photo.setFilePath(rs.getString("file_path")); } catch (SQLException ignored) {}
        return photo;
    }
}


