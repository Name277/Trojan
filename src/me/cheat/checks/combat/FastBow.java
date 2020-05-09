/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.Vector
 */
package me.cheat.checks.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.cheat.Trojan;
import me.cheat.checks.Check;

public class FastBow
extends Check {
    public Map<UUID, Long> bowPull = new HashMap<UUID, Long>();

    public FastBow(Trojan trojan) {
        super("FastBow", "FastBow", trojan);
        this.setBannable(true);
        this.setViolationsToNotify(2);
        this.setEnabled(true);
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void Interact(PlayerInteractEvent e) {
        Player Player2 = e.getPlayer();
        if (Player2.getItemInHand() != null && Player2.getItemInHand().getType().equals((Object)Material.BOW)) {
            this.bowPull.put(Player2.getUniqueId(), System.currentTimeMillis());
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onShoot(ProjectileLaunchEvent e) {
        Player player;
        Arrow arrow;
        if (e.getEntity() instanceof Arrow && (arrow = (Arrow)e.getEntity()).getShooter() != null && arrow.getShooter() instanceof Player && this.bowPull.containsKey((player = (Player)arrow.getShooter()).getUniqueId())) {
            Long time = System.currentTimeMillis() - this.bowPull.get(player.getUniqueId());
            double power = arrow.getVelocity().length();
            Long timeLimit = 300L;
            int ping = this.getTrojan().lag.getPing(player);
            if (ping > 400) {
                timeLimit = 150L;
            }
            if (power > 2.5 && time < timeLimit) {
                this.getTrojan().logCheat(this, player, null, new String[0]);
            }
        }
    }
}

