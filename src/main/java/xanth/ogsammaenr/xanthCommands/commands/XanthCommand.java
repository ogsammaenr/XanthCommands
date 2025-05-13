package xanth.ogsammaenr.xanthCommands.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import xanth.ogsammaenr.xanthCommands.XanthCommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class XanthCommand implements CommandExecutor, TabCompleter {
    XanthCommands plugin;

    public XanthCommand(XanthCommands plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            sendHelp(commandSender);
            return true;
        }

        String subCommand = strings[0].toLowerCase();

        switch (subCommand) {
            case "help":
                sendHelp(commandSender);
                break;

            case "reload":
                handleReload(commandSender);
                break;
            case "version":
                handleVersion(commandSender);
                break;
            case "setenchantingtable":
                handleEnchantingTable(commandSender);
                break;
        }

        return true;
    }

    private void handleReload(CommandSender sender) {
        plugin.reloadConfig();
        sender.sendMessage("§aXanthCommands reloaded.");

    }

    private void handleVersion(CommandSender sender) {
        sender.sendMessage(plugin.getDescription().getVersion());
    }


    private void handleEnchantingTable(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location loc = player.getLocation(); // Oyuncunun bulunduğu konumu al
            String worldName = loc.getWorld().getName();
            String locString = loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();

            // config dosyasına yazalım
            plugin.getConfig().set("enchant-table-locations." + worldName, locString);
            plugin.saveConfig();

            player.sendMessage("Büyü masası konumu " + worldName + " dünyasına kaydedildi!");
        } else {
            sender.sendMessage("Bu komutu yalnızca oyuncular kullanabilir.");
        }

    }


    private void sendHelp(CommandSender player) {
        player.sendMessage("§8§m----------------------------------------");
        player.sendMessage("§b§lXanthCommands Yardım:");
        player.sendMessage("§e/xanthcommands version §7- Plugin sürümünü göster");
        player.sendMessage("§e/xanthcommands reload §7- Yapılandırmayı yeniden yükle");
        player.sendMessage("§e/fly §7- Uçma açık/kapalı");
        player.sendMessage("§e/enchant §7- Büyü masası kullan");
        player.sendMessage("§e/craft §7- Çalışma masası kullan");
        player.sendMessage("§e/anvil §7- Örs kullan");
        player.sendMessage("§e/repairhand §7- elindeki eşyayı tamir eder");
        player.sendMessage("§e/repairall §7- envanterindeki eşyaları tamir eder");
        player.sendMessage("§8§m----------------------------------------");

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            List<String> subCommands = Arrays.asList("help", "reload", "version", "setenchantingtable");

            for (String sub : subCommands) {
                if (sub.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(sub);
                }
            }
            return completions;
        }

        return Collections.emptyList();
    }
}
