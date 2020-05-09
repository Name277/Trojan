/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.Block
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

public class Jesus
extends Check {
    private Map<UUID, Long> jesusTicks = new HashMap<UUID, Long>();

    public Jesus(Trojan Janitor2) {
        super("Jesus", "Jesus", Janitor2);
        this.setBannable(true);
        this.setEnabled(true);
    }
    
    @Override
    public void onEnable() {
    	
    }

    @EventHandler
    public void CheckJesus(PlayerMoveEvent event) {
        if (event.getFrom().getX() == event.getTo().getX() && event.getFrom().getZ() == event.getTo().getZ()) {
            return;
        }
        long Time = System.currentTimeMillis();
        Player player = event.getPlayer();
        if (player.getAllowFlight()) {
            return;
        }
        if (!player.getNearbyEntities(1.0, 1.0, 1.0).isEmpty()) {
            return;
        }
        if (this.jesusTicks.containsKey(player.getUniqueId())) {
            Time = this.jesusTicks.get(player.getUniqueId());
        }
        long MS = System.currentTimeMillis() - Time;
        if (UtilCheat.cantStandAtWater(player.getWorld().getBlockAt(player.getLocation())) && UtilCheat.isHoveringOverWater(player.getLocation()) && !UtilCheat.isFullyInWater(player.getLocation())) {
            if (MS > 500) {
                this.getTrojan().logCheat(this, player, "Jesus", "");
                Time = System.currentTimeMillis();
            }
        } else {
            Time = System.currentTimeMillis();
        }
        this.jesusTicks.put(player.getUniqueId(), Time);
    }
}

