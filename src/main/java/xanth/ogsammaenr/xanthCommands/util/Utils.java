package xanth.ogsammaenr.xanthCommands.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xanth.ogsammaenr.xanthCommands.XanthCommands;

public class Utils {
    /**
     * Oyuncunun sahip olduğu en kısa cooldown süresini (milisaniye cinsinden) döndürür.
     * Süreler config'de dakika cinsindendir.
     *
     * @param player       Oyuncu
     * @param plugin       Plugin referansı
     * @param cooldownPath Config içindeki yol (örnek: "cooldowns.repairhand")
     * @return En kısa cooldown süresi (milisaniye cinsinden)
     */
    public static long getCooldownForPlayer(Player player, XanthCommands plugin, String cooldownPath) {
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection(cooldownPath);

        long selectedCooldownMinutes = Long.MAX_VALUE;

        if (section != null) {
            for (String key : section.getKeys(false)) {
                ConfigurationSection levelSection = section.getConfigurationSection(key);
                if (levelSection == null) continue;

                String permission = levelSection.getString("permission");
                long duration = levelSection.getLong("duration", Long.MAX_VALUE);

                if (permission != null && player.hasPermission(permission)) {
                    if (duration < selectedCooldownMinutes) {
                        selectedCooldownMinutes = duration;
                    }
                }
            }

            // Eğer hiçbir uygun permission yoksa default değer döndür (örnek: 0 dakika)
            if (selectedCooldownMinutes == Long.MAX_VALUE) {
                selectedCooldownMinutes = -1;
            }
        }

        return selectedCooldownMinutes; // dakika → milisaniye
    }

    /**
     * Oyuncunun ilgili cooldown permlerinden en az birine sahip olup olmadığını kontrol eder.
     */
    public static boolean hasCooldownPermission(Player player, JavaPlugin plugin, String cooldownPath) {
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection(cooldownPath);

        if (section != null) {
            for (String perm : section.getKeys(false)) {
                if (perm.equalsIgnoreCase("default")) continue;

                if (player.hasPermission(perm)) {
                    return true;
                }
            }
        }
        return false;
    }
}
