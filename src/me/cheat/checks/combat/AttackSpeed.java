package me.cheat.checks.combat;

import com.comphenix.protocol.wrappers.EnumWrappers;

import me.cheat.Trojan;
import me.cheat.checks.Check;
import me.cheat.packets.events.PacketUseEntityEvent;
import me.cheat.util.UtilTime;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class AttackSpeed
extends Check {
    private Map<UUID, Map.Entry<Integer, Long>> attackTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();

    public AttackSpeed(Trojan trojan) {
        super("AttackSpeed", "AttackSpeed", trojan);
        this.setAutobanTimer(false);
        this.setBannable(false);
        this.setEnabled(true);
    }

    @EventHandler
    public void UseEntity(PacketUseEntityEvent e) {
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK) {
            return;
        }
        if (!(e.getAttacked() instanceof Player)) {
            return;
        }
        Player player = e.getAttacker();
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.attackTicks.containsKey(player.getUniqueId())) {
            Count = this.attackTicks.get(player.getUniqueId()).getKey();
            Time = this.attackTicks.get(player.getUniqueId()).getValue();
        }
        ++Count;
        if (this.attackTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 1000)) {
            if (Count > 19) {
                this.getTrojan().logCheat(this, player, null, String.valueOf(Count) + " ap/s");
            }
            Count = 0;
            Time = UtilTime.nowlong();
        }
        this.attackTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}

