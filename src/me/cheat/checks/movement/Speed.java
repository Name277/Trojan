package me.cheat.checks.movement;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.cheat.Trojan;
import me.cheat.checks.Check;
import me.cheat.util.UtilCheat;
import me.cheat.util.UtilMath;
import me.cheat.util.UtilPlayer;
import me.cheat.util.UtilTime;

public class Speed
extends Check {
    private Map<UUID, Map.Entry<Integer, Long>> speedTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    private Map<UUID, Map.Entry<Integer, Long>> tooFastTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();

    public Speed(Trojan Janitor2) {
        super("Speed", "Speed", Janitor2);
        this.setBannable(true);
        this.setEnabled(true);
    }

    public boolean isOnIce(Player player) {
        Location a = player.getLocation();
        a.setY(a.getY() - 1.0);
        if (a.getBlock().getType().equals((Object)Material.ICE)) {
            return true;
        }
        a.setY(a.getY() - 1.0);
        if (a.getBlock().getType().equals((Object)Material.ICE)) {
            return true;
        }
        return false;
    }

    @Override
    public void onEnable() {
    }

    @EventHandler
    public void CheckSpeed(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.getFrom().getX() == event.getTo().getX() && event.getFrom().getY() == event.getTo().getY() && event.getFrom().getZ() == event.getFrom().getZ()) {
            return;
        }
        if (!this.getTrojan().isEnabled()) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        if (player.getVehicle() != null) {
            return;
        }
        if (this.getTrojan().LastVelocity.containsKey(player.getUniqueId())) {
            return;
        }
        int Count = 0;
        long Time = UtilTime.nowlong();
        if (this.speedTicks.containsKey(player.getUniqueId())) {
            Count = this.speedTicks.get(player.getUniqueId()).getKey();
            Time = this.speedTicks.get(player.getUniqueId()).getValue();
        }
        int TooFastCount = 0;
        if (this.tooFastTicks.containsKey(player.getUniqueId())) {
            float speed;
            double OffsetXZ = UtilMath.offset(UtilMath.getHorizontalVector(event.getFrom().toVector()), UtilMath.getHorizontalVector(event.getTo().toVector()));
            double LimitXZ = 0.0;
            LimitXZ = UtilPlayer.isOnGround(player) && player.getVehicle() == null ? 0.33 : 0.4;
            if (UtilCheat.slabsNear(player.getLocation())) {
                LimitXZ += 0.05;
            }
            Location b = UtilPlayer.getEyeLocation(player);
            b.add(0.0, 1.0, 0.0);
            if (b.getBlock().getType() != Material.AIR && !UtilCheat.canStandWithin(b.getBlock())) {
                LimitXZ = 0.69;
            }
            if (this.isOnIce(player)) {
                LimitXZ = b.getBlock().getType() != Material.AIR && !UtilCheat.canStandWithin(b.getBlock()) ? 1.0 : 0.75;
            }
            LimitXZ += (double)((speed = player.getWalkSpeed()) > 0.2f ? speed * 10.0f * 0.33f : 0.0f);
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (!effect.getType().equals((Object)PotionEffectType.SPEED)) continue;
                if (player.isOnGround()) {
                    LimitXZ += 0.06 * (double)(effect.getAmplifier() + 1);
                    continue;
                }
                LimitXZ += 0.02 * (double)(effect.getAmplifier() + 1);
            }
            if (OffsetXZ > LimitXZ && !UtilTime.elapsed(this.tooFastTicks.get(player.getUniqueId()).getValue(), 150)) {
                TooFastCount = this.tooFastTicks.get(player.getUniqueId()).getKey() + 1;
            } else {
                TooFastCount = 0;
            }
        }
        if (TooFastCount > 6) {
            TooFastCount = 0;
        }
        if (this.speedTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 60000)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count >= 3) {
            Count = 0;
            this.getTrojan().logCheat(this, player, "Speed", new String[0]);
            event.setTo(event.getFrom());
        }
        this.tooFastTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(TooFastCount, System.currentTimeMillis()));
        this.speedTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}

