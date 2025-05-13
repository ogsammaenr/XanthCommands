package xanth.ogsammaenr.xanthCommands;

import org.bukkit.plugin.java.JavaPlugin;
import xanth.ogsammaenr.xanthCommands.commands.*;
import xanth.ogsammaenr.xanthCommands.manager.CoolDownDAO;
import xanth.ogsammaenr.xanthCommands.manager.DatabaseManager;

public final class XanthCommands extends JavaPlugin {

    private DatabaseManager dataBaseManager;
    private CoolDownDAO cooldownDAO;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Veritabanı yöneticisini başlatıyoruz
        dataBaseManager = new DatabaseManager(getDataFolder());
        try {
            dataBaseManager.connect();
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().warning("Veritabanına bağlanılamadı!");
        }

        // CooldownDAO'yu başlatıyoruz
        cooldownDAO = new CoolDownDAO(dataBaseManager);

        getCommand("craft").setExecutor(new CraftCommand());
        getCommand("anvil").setExecutor(new AnvilCommand());
        getCommand("enchant").setExecutor(new EnchantCommand(this));
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("xanthcommands").setExecutor(new XanthCommand(this));
        getCommand("repairhand").setExecutor(new RepairHandCommand(this));
        getCommand("repairall").setExecutor(new RepairAllCommand(this));

        getLogger().info("***** XanthCommands Enabled! *****");

    }

    @Override
    public void onDisable() {
        if (dataBaseManager != null) {
            dataBaseManager.disconnect();
        }
        getLogger().info("***** XanthCommands Disabled! *****");
    }

    public CoolDownDAO getCooldownDAO() {
        return cooldownDAO;
    }
}
