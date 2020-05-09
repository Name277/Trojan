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
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.util.Vector
 */
package me.cheat.checks.combat;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.cheat.Trojan;
import me.cheat.checks.Check;
import me.cheat.util.UtilCheat;
import me.cheat.util.UtilTime;

public class Crits
extends Check {
    private Map<UUID, Map.Entry<Integer, Long>> CritTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    private Map<UUID, Double> FallDistance = new HashMap<UUID, Double>();

    public Crits(Trojan trojan) {
        super("Crits", "Crits", trojan);
        this.setAutobanTimer(false);
        this.setBannable(false);
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
        Player player = (Player)e.getDamager();
        if (player.getAllowFlight()) {
            return;
        }
        if (this.getTrojan().LastVelocity.containsKey(player.getUniqueId())) {
            return;
        }
        if (UtilCheat.slabsNear(player.getLocation())) {
            return;
        }
        Location pL = player.getLocation().clone();
        pL.add(0.0, player.getEyeHeight() + 1.0, 0.0);
        if (UtilCheat.blocksNear(pL)) {
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.CritTicks.containsKey(player.getUniqueId())) {
            Count = this.CritTicks.get(player.getUniqueId()).getKey();
            Time = this.CritTicks.get(player.getUniqueId()).getValue();
        }
        if (!this.FallDistance.containsKey(player.getUniqueId())) {
            return;
        }
        double realFallDistance = this.FallDistance.get(player.getUniqueId());
        Count = (double)player.getFallDistance() > 0.0 && !player.isOnGround() && realFallDistance == 0.0 ? ++Count : 0;
        if (this.CritTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 10000)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count >= 2) {
            Count = 0;
            this.getTrojan().logCheat(this, player, null, new String[0]);
        }
        this.CritTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }

	@EventHandler
    public void Move(PlayerMoveEvent e) {
        Player Player2 = e.getPlayer();
        double Falling = 0.0;
        if (!Player2.isOnGround() && e.getFrom().getY() > e.getTo().getY()) {
            if (this.FallDistance.containsKey(Player2.getUniqueId())) {
                Falling = this.FallDistance.get(Player2.getUniqueId());
            }
            Falling += e.getFrom().getY() - e.getTo().getY();
        }
        this.FallDistance.put(Player2.getUniqueId(), Falling);
    }
}

