package dal;

import model.Photo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository til Photo-objekter. Håndterer CRUD mod dbo.Photos-tabellen.
 */
//Repository til photo-objekter. Håndterer CRUD med Photos i databasen
public class PhotoRepository {

    private static final String URL      = "jdbc:sqlserver://10.176.111.34:1433;databaseName=databaseBelSoft;encrypt=true;trustServerCertificate=true";
    private static final String USER     = "CSe2024a_e_0";
    private static final String PASSWORD = "CSe2024aE0!24";

    private Connection connect() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * //Gemmer et nyt foto med status = 'PENDING'.
     */
    public void save(Photo photo) {

        String sql = "INSERT INTO dbo.Photos (order_number, uploaded_by, status, uploaded_at, comment, file_path) " +
                "VALUES (?, ?, 'in_review', ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, photo.getOrderNumber());
            ps.setString(2, photo.getUploadedBy());
            ps.setTimestamp(3, Timestamp.valueOf(photo.getUploadedAt()));
            ps.setString(4, photo.getComment());
            ps.setString(5, photo.getFilePath());
            ps.executeUpdate();
        } catch (SQLException e) {

            throw new RuntimeException("Kunne ikke gemme foto", e);
        }
    }

    //Finder photos med 'pending'
    public List<Photo> findUnapprovedPhotos() {
        return findByStatus("IN_REVIEW");
    }

   //Finder photos med 'approved'
    public List<Photo> findApprovedPhotos() {
        return findByStatus("APPROVED");
    }

    //Finder et photo med status
    public List<Photo> findByStatus(String status) {

        String sql = "SELECT * FROM dbo.Photos WHERE status = ?";
        List<Photo> result = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    result.add(mapResultSetToPhoto(rs));
                }
            }
        } catch (SQLException e) {

            throw new RuntimeException("Kunne ikke hente fotos med status=" + status, e);
        }
        return result;
    }

    //Finder photos tilhørende en ordrer

    public List<Photo> findByOrderNumber(String orderNumber) {

        String sql = "SELECT * FROM dbo.Photos WHERE order_number = ?";
        List<Photo> result = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, orderNumber);
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    result.add(mapResultSetToPhoto(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Kunne ikke hente fotos for ordre=" + orderNumber, e);
        }
        return result;
    }

    //Opdater status for et photo

    public void updateStatus(int id, boolean approved) {

        String sql = "UPDATE dbo.Photos SET status = ?, approved = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Beregn ny status ud fra boolean approved
            String newStatus = approved ? "APPROVED" : "REJECTED";
            ps.setString(1, newStatus);
            ps.setBoolean(2, approved);
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {

            throw new RuntimeException("Kunne ikke opdatere status for foto-id=" + id, e);
        }
    }

    // Sætter in-review status til et photo
    public void updateStatusToInReview(int id) {

        String sql = "UPDATE dbo.Photos SET status = 'IN_REVIEW' WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {

            throw new RuntimeException("Kunne ikke opdatere status til IN_REVIEW for foto-id=" + id, e);
        }
    }

    // Henter alle fotos, uanset status

    public List<Photo> getAllPhotos() {

        String sql = "SELECT * FROM dbo.Photos";
        List<Photo> photos = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                photos.add(mapResultSetToPhoto(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Kunne ikke hente alle fotos", e);
        }
        return photos;
    }

    //Mapper til et photo
    private Photo mapResultSetToPhoto(ResultSet rs) throws SQLException {

        int id = rs.getInt("id");
        String orderNum = rs.getString("order_number");
        String uploadedBy = rs.getString("uploaded_by");
        String status = rs.getString("status");
        LocalDateTime uploadedAt = rs.getTimestamp("uploaded_at").toLocalDateTime();
        boolean approved = "APPROVED".equalsIgnoreCase(status);
        String comment = rs.getString("comment");

        Photo photo = new Photo(
                id,
                orderNum,
                uploadedBy,
                status,
                uploadedAt,
                approved,
                comment
        );
        photo.setFilePath(rs.getString("file_path"));
        return photo;
    }
}







