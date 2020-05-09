/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.plugin.Plugin
 */
package me.cheat.checks.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

import me.cheat.Trojan;
import me.cheat.checks.Check;
import me.cheat.packets.events.PacketSwingArmEvent;
import me.cheat.util.UtilTime;

public class NoSwing
extends Check {
    private Map<UUID, Long> LastArmSwing = new HashMap<UUID, Long>();

    public NoSwing(Trojan trojan) {
        super("NoSwing", "NoSwing", trojan);
        this.setBannable(true);
        this.setEnabled(true);
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        if (!e.getCause().equals((Object)EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            return;
        }
        if (this.getTrojan().getLag().getTPS() < 17.0) {
            return;
        }
        final Player fplayer = (Player)e.getDamager();
        if (this.getTrojan().isEnabled()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.getTrojan(), new Runnable(){

                @Override
                public void run() {
                    if (!NoSwing.this.hasSwung(fplayer, 1500L)) {
                        NoSwing.this.getTrojan().logCheat(NoSwing.this, fplayer, "NoSwing", new String[0]);
                    }
                }
            }, 10);
        }
    }

    public boolean hasSwung(Player player, Long time) {
        if (!this.LastArmSwing.containsKey(player.getUniqueId())) {
            return false;
        }
        if (UtilTime.nowlong() < this.LastArmSwing.get(player.getUniqueId()) + time) {
            return true;
        }
        return false;
    }

    @EventHandler
    public void ArmSwing(PacketSwingArmEvent event) {
        this.LastArmSwing.put(event.getPlayer().getUniqueId(), UtilTime.nowlong());
    }

}

