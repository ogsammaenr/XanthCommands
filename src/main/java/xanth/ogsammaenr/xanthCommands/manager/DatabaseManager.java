package xanth.ogsammaenr.xanthCommands.manager;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private final File databaseFile;
    private Connection connection;

    public DatabaseManager(File databaseFile) {
        this.databaseFile = new File(databaseFile, "data.db");
    }

    public void connect() throws SQLException {
        if (!databaseFile.exists()) {
            try {
                databaseFile.getParentFile().mkdirs();
                databaseFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String url = "jdbc:sqlite:" + databaseFile.getAbsolutePath();
        connection = DriverManager.getConnection(url);

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS cooldowns (" +
                    "uuid TEXT NOT NULL," +
                    "command TEXT NOT NULL," +
                    "last_used BIGINT," +
                    "PRIMARY KEY (uuid, command))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
