package me.cheat.checks.movement;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import me.cheat.Trojan;
import me.cheat.checks.Check;
import me.cheat.util.UtilCheat;
import me.cheat.util.UtilPlayer;

public class Step
extends Check {
    public Step(Trojan trojan) {
        super("Step", "Step", trojan);
        this.setBannable(false);
        this.setMaxViolations(5);
        this.setEnabled(true);
    }

    public boolean isOnGround(Player player) {
        if (UtilPlayer.isOnClimbable(player)) {
            return false;
        }
        if (player.getVehicle() != null) {
            return false;
        }
        Material type = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
        if (type != Material.AIR && type.isBlock() && type.isSolid() && type != Material.LADDER && type != Material.VINE) {
            return true;
        }
        Location a = player.getLocation().clone();
        a.setY(a.getY() - 0.5);
        type = a.getBlock().getType();
        if (type != Material.AIR && type.isBlock() && type.isSolid() && type != Material.LADDER && type != Material.VINE) {
            return true;
        }
        a = player.getLocation().clone();
        a.setY(a.getY() + 0.5);
        type = a.getBlock().getRelative(BlockFace.DOWN).getType();
        if (type != Material.AIR && type.isBlock() && type.isSolid() && type != Material.LADDER && type != Material.VINE) {
            return true;
        }
        if (UtilCheat.isBlock(player.getLocation().getBlock().getRelative(BlockFace.DOWN), new Material[]{Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER})) {
            return true;
        }
        return false;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!this.isOnGround(player)) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        if (UtilCheat.slabsNear(player.getLocation())) {
            return;
        }
        if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            return;
        }
        if (this.getTrojan().LastVelocity.containsKey(player.getUniqueId())) {
            return;
        }
        double yDist = event.getTo().getY() - event.getFrom().getY();
        if (yDist > 0.9) {
            this.getTrojan().logCheat(this, player, "Step", String.valueOf(Math.round(yDist)));
            event.setTo(event.getFrom());
        }
    }
}

