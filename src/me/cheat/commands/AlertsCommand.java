package me.cheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.cheat.Trojan;
import me.cheat.util.C;

public class AlertsCommand implements CommandExecutor {
	
    private Trojan trojan;

    public AlertsCommand(Trojan trojan) {
        this.trojan = trojan;
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You have to be a player to run this command!");
            return true;
        }
        Player player = (Player)sender;
        if (!player.hasPermission("Core.Mod")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to perform this command."));
            return true;
        }
        if (this.trojan.hasAlertsOn(player)) {
            this.trojan.toggleAlerts(player);
            player.sendMessage(String.valueOf(Trojan.PREFIX) + "Alerts toggled " + C.Red + "OFF");
        } else {
            this.trojan.toggleAlerts(player);
            player.sendMessage(String.valueOf(Trojan.PREFIX) + "Alerts toggled " + C.Green + "ON");
        }
        return true;
    }
}

