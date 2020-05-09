package me.cheat.checks.other;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import me.cheat.Trojan;
import me.cheat.checks.Check;
import me.cheat.packets.events.PacketEntityActionEvent;
import me.cheat.util.UtilTime;

public class Sneak
extends Check {
    private Map<UUID, Map.Entry<Integer, Long>> sneakTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();

    public Sneak(Trojan trojan) {
        super("Sneak", "Sneak", trojan);
        this.setBannable(true);
        this.setEnabled(true);
    }

    @EventHandler
    public void EntityAction(PacketEntityActionEvent event) {
        if (event.getAction() != 1) {
            return;
        }
        Player player = event.getPlayer();
        int Count = 0;
        long Time = -1;
        if (this.sneakTicks.containsKey(player.getUniqueId())) {
            Count = this.sneakTicks.get(player.getUniqueId()).getKey();
            Time = this.sneakTicks.get(player.getUniqueId()).getValue();
        }
        ++Count;
        if (this.sneakTicks.containsKey(player.getUniqueId())) {
            if (UtilTime.elapsed(Time, 100)) {
                Count = 0;
                Time = System.currentTimeMillis();
            } else {
                Time = System.currentTimeMillis();
            }
        }
        if (Count > 50) {
            Count = 0;
            this.getTrojan().logCheat(this, player, "Sneak", new String[0]);
        }
        this.sneakTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}

