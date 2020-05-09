/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.util.Vector
 */
package me.cheat.checks.combat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import me.cheat.Trojan;
import me.cheat.checks.Check;
import me.cheat.util.UtilPlayer;

public class TriggerbotA extends Check {
	
    public TriggerbotA(Trojan trojan) {
        super("Triggerbot", "Triggerbot", trojan);
        this.setBannable(true);
        this.setEnabled(true);
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!e.getCause().equals((Object)EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            return;
        }
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player damager = (Player)e.getDamager();
        Player player = (Player)e.getEntity();
        if (damager.getAllowFlight()) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        double MaxReach = 4.1 + player.getVelocity().length() * 4.0;
        if (damager.getVelocity().length() > 0.08) {
            MaxReach += damager.getVelocity().length();
        }
        double Reach2 = UtilPlayer.getEyeLocation(damager).distance(player.getLocation());
        int Ping = this.getTrojan().getLag().getPing(damager);
        if (Ping >= 100 && Ping < 200) {
            MaxReach += 0.2;
        } else if (Ping >= 200 && Ping < 250) {
            MaxReach += 0.4;
        } else if (Ping >= 250 && Ping < 300) {
            MaxReach += 0.7;
        } else if (Ping >= 300 && Ping < 350) {
            MaxReach += 1.0;
        } else if (Ping >= 350 && Ping < 400) {
            MaxReach += 1.4;
        } else if (Ping > 400) {
            return;
        }
        if (damager.getLocation().getY() > player.getLocation().getY()) {
            double Difference = damager.getLocation().getY() - player.getLocation().getY();
            MaxReach += Difference / 4.0;
        } else if (player.getLocation().getY() > damager.getLocation().getY()) {
            double Difference = player.getLocation().getY() - damager.getLocation().getY();
            MaxReach += Difference / 4.0;
        }
        if (Reach2 > MaxReach) {
            this.getTrojan().logCheat(this, damager, "TriggerBot", new String[0]);
        }
    }

    public class ReachEntry {
        public Long LastTime;
        public List<Double> Reachs;

        public ReachEntry(Long LastTime, List<Double> Reachs) {
            this.Reachs = new ArrayList<Double>();
            this.LastTime = LastTime;
            this.Reachs = Reachs;
        }

        public Long getLastTime() {
            return this.LastTime;
        }

        public List<Double> getReachs() {
            return this.Reachs;
        }

        public void setLastTime(Long LastTime) {
            this.LastTime = LastTime;
        }

        public void setReachs(List<Double> Reachs) {
            this.Reachs = Reachs;
        }

        public void addReach(Double Reach2) {
            this.Reachs.add(Reach2);
        }
    }

}

