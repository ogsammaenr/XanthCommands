package xanth.ogsammaenr.xanthCommands.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;
import xanth.ogsammaenr.xanthCommands.XanthCommands;
import xanth.ogsammaenr.xanthCommands.manager.CoolDownDAO;
import xanth.ogsammaenr.xanthCommands.util.Utils;

import java.util.UUID;

public class RepairHandCommand implements CommandExecutor {
    private final XanthCommands plugin;
    private final CoolDownDAO cooldownDAO;

    public RepairHandCommand(XanthCommands plugin) {
        this.plugin = plugin;
        this.cooldownDAO = plugin.getCooldownDAO();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("bu komut sadece oyuncular tarafından kullanılabilir");
            return true;
        }
        Player player = (Player) commandSender;
        UUID playerUUID = player.getUniqueId();

        FileConfiguration config = plugin.getConfig(); // Ana sınıftan alınmalı
        long cooldownMinutes = Utils.getCooldownForPlayer(player, plugin, "cooldowns.repairhand");

        if (cooldownMinutes == -1) {
            player.sendMessage("§cBu komutu kullanmak için izniniz yok.");
            return true;
        }

        long cooldownMillis = cooldownMinutes * 60L * 1000L;
        long lastUsed = cooldownDAO.getLastUsedTime(playerUUID, "repairhand");
        long now = System.currentTimeMillis();


        if (now - lastUsed < cooldownMillis) {
            long seconds = (cooldownMillis - (now - lastUsed)) / 1000;
            long hours = seconds / 3600;
            long minutes = (seconds % 3600) / 60;
            long secs = seconds % 60;

            String timeMessage;
            if (hours > 0) {
                timeMessage = hours + " saat " + minutes + " dakika " + secs + " saniye";
            } else {
                timeMessage = minutes + " dakika " + secs + " saniye";
            }

            player.sendMessage("§eBu komutu tekrar kullanmak için " + timeMessage + " beklemelisiniz.");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage("§eBu komudu kullanmak için elinde bir eşya bulunması lazım");
            return true;
        }

        if (item.getItemMeta() instanceof Damageable damageable) {
            if (damageable.getDamage() == 0) {
                player.sendMessage("§eElinizdeki eşya hasar almamış");
                return true;
            }

            item.setDurability((short) 0);
            player.sendMessage("§aElinizdeki eşya tamir edildi");
            cooldownDAO.setLastUsedTime(playerUUID, "repairhand", now);


        } else {
            player.sendMessage("elinizdeki eşya tamir edilemez");
        }
        return true;
    }
}
