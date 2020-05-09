/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerMoveEvent
 */
package me.cheat.checks.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import me.cheat.Trojan;
import me.cheat.checks.Check;
import me.cheat.util.UtilCheat;
import me.cheat.util.UtilPlayer;

public class Fly
extends Check {
    private Map<UUID, Long> flyTicks = new HashMap<UUID, Long>();

    public Fly(Trojan Janitor2) {
        super("Fly", "Fly", Janitor2);
        this.setBannable(true);
        this.setEnabled(true);
    }

    @EventHandler
    public void CheckFly(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getAllowFlight()) {
            return;
        }
        if (player.getVehicle() != null) {
            return;
        }
        if (UtilPlayer.isInWater(player)) {
            return;
        }
        if (UtilCheat.isInWeb(player)) {
            return;
        }
        if (UtilCheat.blocksNear(player)) {
            if (this.flyTicks.containsKey(player.getUniqueId())) {
                this.flyTicks.remove(player.getUniqueId());
            }
            return;
        }
        if (event.getTo().getX() == event.getFrom().getX() && event.getTo().getZ() == event.getFrom().getZ()) {
            return;
        }
        if (event.getTo().getY() != event.getFrom().getY()) {
            if (this.flyTicks.containsKey(player.getUniqueId())) {
                this.flyTicks.remove(player.getUniqueId());
            }
            return;
        }
        long Time = System.currentTimeMillis();
        if (this.flyTicks.containsKey(player.getUniqueId())) {
            Time = this.flyTicks.get(player.getUniqueId());
        }
        if ((System.currentTimeMillis() - Time) > 500) {
            this.flyTicks.remove(player.getUniqueId());
            this.getTrojan().logCheat(this, player, "Hover", new String[0]);
            event.setTo(event.getFrom());
            return;
        }
        this.flyTicks.put(player.getUniqueId(), Time);
    }
}

