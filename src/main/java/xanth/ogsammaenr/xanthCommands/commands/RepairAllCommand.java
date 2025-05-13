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

public class RepairAllCommand implements CommandExecutor {
    private XanthCommands plugin;
    private CoolDownDAO cooldownDAO;

    public RepairAllCommand(XanthCommands plugin) {
        this.plugin = plugin;
        this.cooldownDAO = plugin.getCooldownDAO();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("bu komudu sadece oyuncular kullanabilir");
            return true;
        }
        Player player = (Player) commandSender;
        UUID playerUUID = player.getUniqueId();

        FileConfiguration config = plugin.getConfig(); // Ana sınıftan alınmalı
        long cooldownMinutes = Utils.getCooldownForPlayer(player, plugin, "cooldowns.repairall");
        if (cooldownMinutes == -1) {
            player.sendMessage("§cBu komutu kullanmak için izniniz yok.");
            return true;
        }
        long cooldownMillis = cooldownMinutes * 60L * 1000L;
        long lastUsed = cooldownDAO.getLastUsedTime(playerUUID, "repairall");
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

        int repairCount = 0;

        ItemStack[] items = player.getInventory().getContents();

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if (item == null || item.getType() == Material.AIR) continue;

            if (item.getItemMeta() instanceof Damageable damageable && damageable.getDamage() != 0) {

                item.setDurability((short) 0);
                repairCount++;
            }
        }

        if (repairCount > 0) {
            player.sendMessage("§a" + repairCount + " eşya başarıyla tamir edildi!");
            cooldownDAO.setLastUsedTime(playerUUID, "repairall", now);
        } else {
            player.sendMessage("§c Tamir edilecek eşya bulunamadı!");
        }
        return true;
    }
}
