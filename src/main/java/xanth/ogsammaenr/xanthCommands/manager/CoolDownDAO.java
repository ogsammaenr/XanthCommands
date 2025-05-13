package xanth.ogsammaenr.xanthCommands.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CoolDownDAO {
    private final DatabaseManager databaseManager;

    public CoolDownDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public long getLastUsedTime(UUID uuid, String command) {
        String sql = "SELECT last_used FROM cooldowns WHERE uuid = ? AND command = ?;";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, uuid.toString());
            statement.setString(2, command);

            ResultSet resultSet = statement.executeQuery();

            System.out.println("Cooldown check for " + uuid + " command: " + command + " returned: " + resultSet.getLong("last_used"));
            if (resultSet.next()) {
                return resultSet.getLong("last_used");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0L; // Eğer kaydı bulamazsa 0 döndürüyoruz
    }

    public void setLastUsedTime(UUID uuid, String command, long time) {
        String sql = "INSERT INTO cooldowns (uuid, command, last_used) VALUES (?, ?, ?) " +
                "ON CONFLICT(uuid, command) DO UPDATE SET last_used = excluded.last_used;";

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, uuid.toString());
            statement.setString(2, command);
            statement.setLong(3, time);
            statement.executeUpdate();
            System.out.println("Cooldown set for " + uuid + " command: " + command + " at time: " + time);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearCooldown(UUID uuid, String command) {
        String sql = "DELETE FROM cooldowns WHERE uuid = ? AND command = ?;";

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, uuid.toString());
            statement.setString(2, command);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
