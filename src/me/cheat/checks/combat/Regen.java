package me.cheat.checks.combat;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Difficulty;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import me.cheat.Trojan;
import me.cheat.checks.Check;
import me.cheat.util.UtilTime;

public class Regen
extends Check {
    private Map<UUID, Long> LastHeal = new HashMap<UUID, Long>();
    private Map<UUID, Map.Entry<Integer, Long>> FastHealTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();

    public Regen(Trojan trojan) {
        super("Regen", "Regen", trojan);
        this.setBannable(true);
        this.setEnabled(true);
    }

    public boolean checkFastHeal(Player player) {
        if (this.LastHeal.containsKey(player.getUniqueId())) {
            long l = this.LastHeal.get(player.getUniqueId());
            this.LastHeal.remove(player.getUniqueId());
            if (System.currentTimeMillis() - l < 3000) {
                return true;
            }
        }
        return false;
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onHeal(EntityRegainHealthEvent event) {
        if (!event.getRegainReason().equals((Object)EntityRegainHealthEvent.RegainReason.SATIATED)) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getEntity();
        if (player.getWorld().getDifficulty().equals((Object)Difficulty.PEACEFUL)) {
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.FastHealTicks.containsKey(player.getUniqueId())) {
            Count = this.FastHealTicks.get(player.getUniqueId()).getKey();
            Time = this.FastHealTicks.get(player.getUniqueId()).getValue();
        }
        if (this.checkFastHeal(player)) {
            this.getTrojan().logCheat(this, player, "Regen", new String[0]);
        }
        if (this.FastHealTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 60000)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        this.LastHeal.put(player.getUniqueId(), System.currentTimeMillis());
        this.FastHealTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}

