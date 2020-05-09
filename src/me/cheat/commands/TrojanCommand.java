package me.cheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.cheat.Trojan;
import me.cheat.checks.Check;
import me.cheat.util.C;

public class TrojanCommand implements CommandExecutor {
	
  private Trojan trojan;
  
  public TrojanCommand(Trojan trojan) {
    this.trojan = trojan;
  }
  
  public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
	  
    if (!sender.hasPermission("Core.Owner")) {
      sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to perform this command."));
      return true;
    }
    
    if (args.length == 0) {
      sender.sendMessage(C.Red + "/trojan toggle <check>");
      sender.sendMessage(C.Red + "/trojan bannable <check>");
      return true;
    }
    
    if (args.length >= 2) {
    	
      String type = args[0];
      
        if (type.equals("bannable")) {
        	
            if (!sender.hasPermission("Core.Owner"))
            {
              sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to perform this command."));
              return true;
            }
        	
            String name11 = "";
            
            for (int i = 1; i < args.length; i++) {
              name11 = name11 + args[i] + (args.length - 1 == i ? "" : " ");
            }
            
            for (Check check : this.trojan.getChecks()) {
              if (check.getIdentifier().equalsIgnoreCase(name11))
              {
                if (check.isBannable())
                {
                  sender.sendMessage(Trojan.PREFIX + C.Gray + "The check " + check.getName() + " is now " + C.Green + "not bannable" + C.Gray + ".");
                  check.setBannable(false);
                  break;
                }
                sender.sendMessage(Trojan.PREFIX + C.Gray + "The check " + check.getName() + " is now " + C.Red + "bannable" + C.Gray + ".");
                check.setBannable(true);
                
                break;
              }
            }
        }

        if (type.equals("toggle")) {
        	
            if (!sender.hasPermission("Core.Owner"))
            {
              sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to perform this command."));
              return true;
            }
        	
            String name1 = "";
            for (int i = 1; i < args.length; i++) {
              name1 = name1 + args[i] + (args.length - 1 == i ? "" : " ");
            }
            for (Check check : this.trojan.getChecks()) {
              if (check.getIdentifier().equalsIgnoreCase(name1))
              {
                if (check.isEnabled())
                {
                  sender.sendMessage(Trojan.PREFIX + C.Gray + "The check " + check.getName() + " is now " + C.Red + "disabled" + C.Gray + ".");
                  check.setEnabled(false);
                  break;
                }
                sender.sendMessage(Trojan.PREFIX + C.Gray + "The check " + check.getName() + " is now " + C.Green + "enabled" + C.Gray + ".");
                check.setEnabled(true);
                
                break;
              }
            }
          }
        } else {
          if (!(sender instanceof Player)) {
            sender.sendMessage("You have to be a player to run this command!");
            return true;
          }
      }
    return true;
  }
}
