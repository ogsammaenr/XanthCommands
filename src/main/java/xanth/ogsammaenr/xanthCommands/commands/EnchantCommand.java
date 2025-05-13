package xanth.ogsammaenr.xanthCommands.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xanth.ogsammaenr.xanthCommands.XanthCommands;

public class EnchantCommand implements CommandExecutor {

    private final XanthCommands plugin;

    public EnchantCommand(XanthCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Bu komut sadece oyuncular tarafından kullanılabilir.");
            return true;
        }

        FileConfiguration config = plugin.getConfig();
        String worldName = player.getWorld().getName();  // Oyuncunun bulunduğu dünya ismini alıyoruz

        // Config'teki o dünya için büyü masası konumunu alalım
        String locationString = config.getString("enchant-table-locations." + worldName);
        if (locationString == null) {
            player.sendMessage("§cBu dünyada büyü masası kullanamazsın.");
            return true;
        }

        // Konum string'ini parse ediyoruz
        String[] locParts = locationString.split(",");
        if (locParts.length != 3) {
            player.sendMessage("§cBüyü masası konumu hatalı formatta.");
            return true;
        }

        try {
            int x = Integer.parseInt(locParts[0]);
            int y = Integer.parseInt(locParts[1]);
            int z = Integer.parseInt(locParts[2]);

            World world = player.getWorld();  // Oyuncunun bulunduğu dünyayı alıyoruz
            Location location = new Location(world, x, y, z);

            // Büyü masası açılacak
            player.openEnchanting(location, true);
            player.sendMessage("§aBüyü masası açıldı!");
        } catch (NumberFormatException e) {
            player.sendMessage("§cBüyü masası koordinatları hatalı.");
        }

        return true;
    }
}
