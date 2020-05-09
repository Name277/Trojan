/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.EnumWrappers
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityUseAction
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.util.Vector
 */
package me.cheat.checks.combat;

import com.comphenix.protocol.wrappers.EnumWrappers;

import me.cheat.Trojan;
import me.cheat.checks.Check;
import me.cheat.packets.events.PacketUseEntityEvent;
import me.cheat.util.UtilCheat;
import me.cheat.util.UtilTime;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class KillAuraB
extends Check {
    private Map<UUID, Map.Entry<Integer, Long>> AuraTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();

    public KillAuraB(Trojan trojan) {
        super("KillAuraB", "Kill Aura (Type B)", trojan);
        this.setBannable(true);
        this.setEnabled(true);
    }

    @Override
    public void onEnable() {
    }

    @EventHandler
    public void UseEntity(PacketUseEntityEvent e) {
        int Ping;
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK) {
            return;
        }
        if (!(e.getAttacked() instanceof Player)) {
            return;
        }
        Player damager = e.getAttacker();
        Player player = (Player)e.getAttacked();
        if (damager.getAllowFlight()) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.AuraTicks.containsKey(damager.getUniqueId())) {
            Count = this.AuraTicks.get(damager.getUniqueId()).getKey();
            Time = this.AuraTicks.get(damager.getUniqueId()).getValue();
        }
        double OffsetXZ = UtilCheat.getAimbotoffset(damager.getLocation(), damager.getEyeHeight(), (LivingEntity)player);
        double LimitOffset = 200.0;
        if (damager.getVelocity().length() > 0.08 || this.getTrojan().LastVelocity.containsKey(damager.getUniqueId())) {
            LimitOffset += 200.0;
        }
        if ((Ping = this.getTrojan().getLag().getPing(damager)) >= 100 && Ping < 200) {
            LimitOffset += 50.0;
        } else if (Ping >= 200 && Ping < 250) {
            LimitOffset += 75.0;
        } else if (Ping >= 250 && Ping < 300) {
            LimitOffset += 150.0;
        } else if (Ping >= 300 && Ping < 350) {
            LimitOffset += 300.0;
        } else if (Ping >= 350 && Ping < 400) {
            LimitOffset += 400.0;
        } else if (Ping > 400) {
            return;
        }

        if (OffsetXZ > LimitOffset * 4.0) {
            Count += 12;
        } else if (OffsetXZ > LimitOffset * 3.0) {
            Count += 10;
        } else if (OffsetXZ > LimitOffset * 2.0) {
            Count += 8;
        } else if (OffsetXZ > LimitOffset) {
            Count += 4;
        }
        if (this.AuraTicks.containsKey(damager.getUniqueId()) && UtilTime.elapsed(Time, 60000)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count >= 16) {
            Count = 0;
            this.getTrojan().logCheat(this, damager, "Hit Miss Ratio", "");
        }
        this.AuraTicks.put(damager.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}

