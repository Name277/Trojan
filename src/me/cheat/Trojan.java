package me.cheat;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import me.cheat.checks.Check;
import me.cheat.checks.combat.AttackSpeed;
import me.cheat.checks.combat.Crits;
import me.cheat.checks.combat.DoubleClick;
import me.cheat.checks.combat.FastBow;
import me.cheat.checks.combat.KillAuraA;
import me.cheat.checks.combat.KillAuraB;
import me.cheat.checks.combat.KillAuraC;
import me.cheat.checks.combat.KillAuraE;
import me.cheat.checks.combat.NoSwing;
import me.cheat.checks.combat.Reach;
import me.cheat.checks.combat.Regen;
import me.cheat.checks.combat.TriggerbotA;
import me.cheat.checks.combat.Twitch;
import me.cheat.checks.combat.VelocityVertical;
import me.cheat.checks.movement.Ascension;
import me.cheat.checks.movement.Fly;
import me.cheat.checks.movement.Glide;
import me.cheat.checks.movement.Jesus;
import me.cheat.checks.movement.NoFall;
import me.cheat.checks.movement.Phase;
import me.cheat.checks.movement.Speed;
import me.cheat.checks.movement.Step;
import me.cheat.checks.movement.VClip;
import me.cheat.checks.other.MorePackets;
import me.cheat.checks.other.Sneak;
import me.cheat.checks.other.Timer;
import me.cheat.commands.AlertsCommand;
import me.cheat.commands.AutobanCommand;
import me.cheat.commands.TrojanCommand;
import me.cheat.lag.LagCore;
import me.cheat.packets.PacketCore;
import me.cheat.update.UpdateType;
import me.cheat.update.Updater;
import me.cheat.update.events.UpdateEvent;
import me.cheat.util.C;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Trojan extends JavaPlugin implements Listener {
	
    public static Trojan Instance;
    public static String PREFIX;
    public Updater updater;
    public PacketCore packet;
    public LagCore lag;
    public List<Check> Checks = new ArrayList<Check>();
    public Map<UUID, Map<Check, Integer>> Violations = new HashMap<UUID, Map<Check, Integer>>();
    public Map<UUID, Map<Check, Long>> ViolationReset = new HashMap<UUID, Map<Check, Long>>();
    public List<Player> AlertsOn = new ArrayList<Player>();
    public Map<Player, Map.Entry<Check, Long>> AutoBan = new HashMap<Player, Map.Entry<Check, Long>>();
    Random rand = new Random();
    public Map<UUID, Map.Entry<Long, Vector>> LastVelocity = new HashMap<UUID, Map.Entry<Long, Vector>>();

    static {
        PREFIX = ChatColor.translateAlternateColorCodes('&', "&7[&c&l!&7] ");
    }

    public void onEnable() {
        Instance = this;
        this.updater = new Updater(this);
        this.packet = new PacketCore(this);
        this.lag = new LagCore(this);
        this.Checks.add(new Jesus(this));
        this.Checks.add(new Ascension(this));
        this.Checks.add(new Speed(this));
        this.Checks.add(new Glide(this));
        this.Checks.add(new Fly(this));
        this.Checks.add(new Regen(this));
        this.Checks.add(new NoFall(this));
        this.Checks.add(new Step(this));
        this.Checks.add(new VClip(this));
        this.Checks.add(new Phase(this));
        this.Checks.add(new DoubleClick(this));
        this.Checks.add(new KillAuraA(this));
        this.Checks.add(new KillAuraB(this));
        this.Checks.add(new KillAuraC(this));
        this.Checks.add(new KillAuraE(this));
        this.Checks.add(new AttackSpeed(this));
        this.Checks.add(new NoSwing(this));
        this.Checks.add(new FastBow(this));
        this.Checks.add(new Twitch(this));
        this.Checks.add(new VelocityVertical(this));
        this.Checks.add(new Crits(this));
        this.Checks.add(new Reach(this));
        this.Checks.add(new TriggerbotA(this));
        this.Checks.add(new MorePackets(this));
        this.Checks.add(new Timer(this));
        this.Checks.add(new Sneak(this));
        for (Check check : this.Checks) {
            if (!check.isEnabled()) continue;
            this.RegisterListener(check);
        }
        this.RegisterListener(this);
        this.getCommand("alerts").setExecutor((CommandExecutor)new AlertsCommand(this));
        this.getCommand("autoban").setExecutor((CommandExecutor)new AutobanCommand(this));
        this.getCommand("trojan").setExecutor((CommandExecutor)new TrojanCommand(this));
        
    }

    public List<Check> getChecks() {
        return new ArrayList<Check>(this.Checks);
    }

    public List<Player> getAutobanQueue() {
        return new ArrayList<Player>(this.AutoBan.keySet());
    }

    public void removeFromAutobanQueue(Player player) {
        this.AutoBan.remove((Object)player);
    }

    public void removeViolations(Player player) {
        this.Violations.remove(player.getUniqueId());
    }

    public boolean hasAlertsOn(Player player) {
        return this.AlertsOn.contains((Object)player);
    }

    public void toggleAlerts(Player player) {
        if (this.hasAlertsOn(player)) {
            this.AlertsOn.remove((Object)player);
        } else {
            this.AlertsOn.add(player);
        }
    }
    

    public LagCore getLag() {
        return this.lag;
    }

    @EventHandler
    public void Join(PlayerJoinEvent e) {
    	Player p = e.getPlayer();
        if (!p.hasPermission("Core.Mod")) {
            return;
        }
        this.AlertsOn.add(e.getPlayer());
    }

    @EventHandler
    public void autobanupdate(UpdateEvent event) {
        if (!event.getType().equals((Object)UpdateType.SEC)) {
            return;
        }
        HashMap<Player, Map.Entry<Check, Long>> AutoBan = new HashMap<Player, Map.Entry<Check, Long>>(this.AutoBan);
        for (Player player : AutoBan.keySet()) {
            if (player == null || !player.isOnline()) {
                this.AutoBan.remove((Object)player);
                continue;
            }
            Long time = AutoBan.get((Object)player).getValue();
            if (System.currentTimeMillis() < time) continue;
            this.autobanOver(player);
        }
        HashMap<UUID, Map<Check, Long>> ViolationResets = new HashMap<UUID, Map<Check, Long>>(this.ViolationReset);
        for (UUID uid : ViolationResets.keySet()) {
            if (!this.Violations.containsKey(uid)) continue;
            HashMap<Check, Long> Checks = new HashMap<Check, Long>(ViolationResets.get(uid));
            for (Check check : Checks.keySet()) {
                Long time = Checks.get(check);
                if (System.currentTimeMillis() < time) continue;
                this.ViolationReset.get(uid).remove(check);
                this.Violations.get(uid).remove(check);
            }
        }
    }

    public Integer getViolations(Player player, Check check) {
        if (this.Violations.containsKey(player.getUniqueId())) {
            return this.Violations.get(player.getUniqueId()).get(check);
        }
        return 0;
    }

    public Map<Check, Integer> getViolations(Player player) {
        if (this.Violations.containsKey(player.getUniqueId())) {
            return new HashMap<Check, Integer>(this.Violations.get(player.getUniqueId()));
        }
        return null;
    }

    public void addViolation(Player player, Check check) {
        Map map = new HashMap<Check, Integer>();
        if (this.Violations.containsKey(player.getUniqueId())) {
            map = this.Violations.get(player.getUniqueId());
        }
        if (!map.containsKey(check)) {
            map.put((Check)check, 1);
        } else {
            map.put((Check)check, (Integer)map.get(check) + 1);
        }
        this.Violations.put(player.getUniqueId(), map);
    }

    public void removeViolations(Player player, Check check) {
        if (this.Violations.containsKey(player.getUniqueId())) {
            this.Violations.get(player.getUniqueId()).remove(check);
        }
    }

    public void setViolationResetTime(Player player, Check check, long time) {
        Map map = new HashMap<Check, Long>();
        if (this.ViolationReset.containsKey(player.getUniqueId())) {
            map = this.ViolationReset.get(player.getUniqueId());
        }
        map.put(check, time);
        this.ViolationReset.put(player.getUniqueId(), map);
    }

    public void autobanOver(Player player) {
        HashMap<Player, Map.Entry<Check, Long>> AutoBan = new HashMap<Player, Map.Entry<Check, Long>>(this.AutoBan);
        if (AutoBan.containsKey((Object)player)) {
            this.banPlayer(player, AutoBan.get((Object)player).getKey());
            this.AutoBan.remove((Object)player);
        }
    }

    public void autoban(Check check, Player player) {
        if (this.lag.getTPS() < 12.0) {
            return;
        }
        if (check.hasBanTimer()) {
            if (this.AutoBan.containsKey((Object)player)) {
                return;
            }
            this.AutoBan.put(player, new AbstractMap.SimpleEntry<Check, Long>(check, System.currentTimeMillis() + 15000));
            
            String msg = PREFIX + C.Yellow + player.getName() + C.Gray + " will be banned for " + C.Blue + check.getName() + C.Gray + " in 15 seconds!";
	        TextComponent message = new TextComponent(msg);
	        message.setClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/tp " + player.getName()));
	        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to to teleport").create()));
    	    TextComponent message2 = new TextComponent(" [C] ");
    	    message2.setColor(ChatColor.GREEN);
    	    message2.setBold(true);
    	    message2.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/autoban cancel " + player.getName()) );
    	    message2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to cancel the autoban").create()));
    	    TextComponent message3 = new TextComponent(" [B] ");
    	    message3.setColor(ChatColor.DARK_RED);
    	    message3.setBold(true);
    	    message3.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/autoban ban " + player.getName()) );
    	    message3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to ban now").create()));
    	    message.addExtra(message2);
    	    message.addExtra(message3);
            for (Player s : Bukkit.getOnlinePlayers()) {
                if (s.hasPermission("AntiCheat.Staff")) {
                    s.spigot().sendMessage(message);
                }
            }
        } else {
            this.banPlayer(player, check);
        }
    }

    public void banPlayer(final Player player, Check check) {
        this.Violations.remove(player.getUniqueId());
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this, new Runnable(){

            @Override
            public void run() {
                Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "ban " + player.getName() + " [Trojan] - Unfair Advantage");
            }
        }, 10);
    }

    public void alert(String message) {
        for (Player s : this.AlertsOn) {
            s.sendMessage(String.valueOf(PREFIX) + message);
        }
    }

    public void logCheat(Check check, Player player, String hoverabletext, String ... identifiers) {
        String a = "";
        if (identifiers != null) {
            String[] arrstring = identifiers;
            int n = arrstring.length;
            int n2 = 0;
            while (n2 < n) {
                String b = arrstring[n2];
                a = String.valueOf(a) + " (" + b + ")";
                ++n2;
            }
        }
        this.addViolation(player, check);
        Integer violations = this.getViolations(player, check);
        
        String msg = PREFIX + C.Yellow + player.getName() + C.Gray + " suspected of " + C.Red + check.getName() + C.Gray + " [" + C.Gold + violations + C.Gray + " VL]";
        TextComponent message = new TextComponent(msg);
        message.setClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/tp " + player.getName()));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to to teleport").create()));
        if (violations % check.getViolationsToNotify() == 0) {
            for (Player playerplayer : this.AlertsOn) {
                if (!playerplayer.hasPermission("Core.Mod")) 
                	continue;
                playerplayer.spigot().sendMessage(message);
            }
        }
        this.setViolationResetTime(player, check, System.currentTimeMillis() + check.getViolationResetTime());
        if (violations > check.getMaxViolations() && check.isBannable()) {
            this.autoban(check, player);
        }
    }

    public void onDisable() {
        this.updater.Disable();
    }

    public void RegisterListener(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, (Plugin)this);
    }

    public Map<UUID, Map.Entry<Long, Vector>> getLastVelocity() {
        return new HashMap<UUID, Map.Entry<Long, Vector>>(this.LastVelocity);
    }

    @EventHandler
    public void Velocity(PlayerVelocityEvent event) {
        this.LastVelocity.put(event.getPlayer().getUniqueId(), new AbstractMap.SimpleEntry<Long, Vector>(System.currentTimeMillis(), event.getVelocity()));
    }

    @EventHandler
    public void Update(UpdateEvent event) {
        if (!event.getType().equals((Object)UpdateType.TICK)) {
            return;
        }
        for (UUID uid : this.getLastVelocity().keySet()) {
            Player player = this.getServer().getPlayer(uid);
            if (player == null || !player.isOnline()) {
                this.LastVelocity.remove(uid);
                continue;
            }
            Vector velocity = this.getLastVelocity().get(uid).getValue();
            Long time = this.getLastVelocity().get(uid).getKey();
            if (time + 500 > System.currentTimeMillis()) continue;
            double velY = velocity.getY() * velocity.getY();
            double Y = player.getVelocity().getY() * player.getVelocity().getY();
            if (Y < 0.02) {
                this.LastVelocity.remove(uid);
                continue;
            }
            if (Y <= velY * 3.0) continue;
            this.LastVelocity.remove(uid);
        }
    }

}

