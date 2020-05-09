package me.cheat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.cheat.Trojan;
import me.cheat.util.C;

public class AutobanCommand
  implements CommandExecutor
{
  private Trojan trojan;
  
  public AutobanCommand(Trojan trojan)
  {
    this.trojan = trojan;
  }
  
  public boolean onCommand(CommandSender sender, Command command, String alias, String[] args)
  {
    if (!sender.hasPermission("Core.Admin"))
    {
      sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to perform this command."));
      return true;
    }
    if (args.length == 2)
    {
      String type = args[0];
      String playerName = args[1];
      Player player = Bukkit.getServer().getPlayer(playerName);
      if ((player == null) || (!player.isOnline()))
      {
        sender.sendMessage(C.Red + "Error: Player not found!");
        return true;
      }
      if (this.trojan.getAutobanQueue().contains(player)) {
       if (type.equals("cancel")) {
           this.trojan.removeFromAutobanQueue(player);
           this.trojan.removeViolations(player);
           Bukkit.broadcast(Trojan.PREFIX + C.Yellow + player.getName() + C.Gray + " auto-ban has been cancelled by " + C.Yellow + sender.getName() + C.Gray + ".", "AntiCheat.Staff");
       }
       else if (type.equals("ban")) {
         Bukkit.broadcast(Trojan.PREFIX + C.Yellow + player.getName() + C.Gray + "'s auto-ban has been forced by " + C.Yellow + sender.getName() + C.Gray + ".", "AntiCheat.Staff");
         this.trojan.autobanOver(player);
       }
      }
      else
      {
        sender.sendMessage(C.Red + "This player is not in the autoban queue!");
      }
    }
    return true;
  }
}
