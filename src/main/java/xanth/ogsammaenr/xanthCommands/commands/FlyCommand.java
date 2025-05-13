package xanth.ogsammaenr.xanthCommands.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Bu komut sadece oyuncular tarafından kullanılabilir.");
            return true;
        }
        boolean newState = !player.getAllowFlight();
        player.setAllowFlight(newState);
        player.setFlying(newState);
        if (newState) {
            player.sendMessage("§aUçuş modu açıldı");
        } else {
            player.sendMessage("§cUçuş modu kapandı");
        }
        return true;


    }
}
