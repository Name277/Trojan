/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerMoveEvent
 */
package me.cheat.checks.movement;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import me.cheat.Trojan;
import me.cheat.checks.Check;
import me.cheat.util.UtilPlayer;
import me.cheat.util.UtilTime;

public class NoFall
extends Check {
    private Map<UUID, Map.Entry<Long, Integer>> NoFallTicks = new HashMap<UUID, Map.Entry<Long, Integer>>();
    private Map<UUID, Double> FallDistance = new HashMap<UUID, Double>();

    public NoFall(Trojan trojan) {
        super("NoFall", "NoFall", trojan);
        this.setBannable(false);
        this.setEnabled(true);
    }
    

    @EventHandler
    public void Move(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getAllowFlight()) {
            return;
        }
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return;
        }
        if (player.getVehicle() != null) {
            return;
        }
        if (UtilPlayer.isOnClimbable(player)) {
            return;
        }
        if (UtilPlayer.isInWater(player)) {
            return;
        }
        double Falling = 0.0;
        if (!UtilPlayer.isOnGround(player) && e.getFrom().getY() > e.getTo().getY()) {
            if (this.FallDistance.containsKey(player.getUniqueId())) {
                Falling = this.FallDistance.get(player.getUniqueId());
            }
            Falling += e.getFrom().getY() - e.getTo().getY();
        }
        this.FallDistance.put(player.getUniqueId(), Falling);
        if (Falling < 3.0) {
            return;
        }
        long Time = System.currentTimeMillis();
        int Count = 0;
        if (this.NoFallTicks.containsKey(player.getUniqueId())) {
            Time = this.NoFallTicks.get(player.getUniqueId()).getKey();
            Count = this.NoFallTicks.get(player.getUniqueId()).getValue();
        }
        if (player.isOnGround() || player.getFallDistance() == 0.0f) {
            ++Count;
        } else {
            Count = 0;
        }
        if (this.NoFallTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 10000)) {
            Count = 0;
            Time = System.currentTimeMillis();
        }
        if (Count >= 3) {
            Count = 0;
            this.FallDistance.put(player.getUniqueId(), 0.0);
            this.getTrojan().logCheat(this, player, "NoFall", new String[0]);
        }
        this.NoFallTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Long, Integer>(Time, Count));
    }
}

