package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnector {

    //
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=databaseBelSoft;encrypt=true;trustServerCertificate=true";
    private static final String USER = "CSe2024a_e_0"; //
    private static final String PASSWORD = "CSe2024aE0!24"; //

    /**
     * Returnerer en SQL-forbindelse.
     * @return Connection objekt, hvis succes, ellers null
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
}
