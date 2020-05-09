/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 */
package me.cheat.checks.combat;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import me.cheat.Trojan;
import me.cheat.checks.Check;

public class KillAuraE
extends Check {
    private Map<Player, Map.Entry<Integer, Long>> lastAttack = new HashMap<Player, Map.Entry<Integer, Long>>();

    public KillAuraE(Trojan trojan) {
        super("KillAuraE", "Kill Aura (Type E)", trojan);
        this.setBannable(true);
        this.setEnabled(true);
    }

    @Override
    public void onEnable() {
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void Damage(EntityDamageByEntityEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)e.getDamager();
        if (this.lastAttack.containsKey((Object)player)) {
            Integer entityid = this.lastAttack.get((Object)player).getKey();
            Long time = this.lastAttack.get((Object)player).getValue();
            if (entityid.intValue() != e.getEntity().getEntityId() && System.currentTimeMillis() - time < 5) {
                this.getTrojan().logCheat(this, player, "MultiAura", new String[0]);
            }
            this.lastAttack.remove((Object)player);
        } else {
            this.lastAttack.put(player, new AbstractMap.SimpleEntry<Integer, Long>(e.getEntity().getEntityId(), System.currentTimeMillis()));
        }
    }
}

