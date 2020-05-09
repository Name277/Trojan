package me.cheat.checks.other;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.cheat.Trojan;
import me.cheat.checks.Check;
import me.cheat.packets.events.PacketPlayerEvent;
import me.cheat.util.UtilTime;

public class MorePackets
extends Check {
    private Map<UUID, Map.Entry<Integer, Long>> packetTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    private Map<UUID, Long> lastPacket = new HashMap<UUID, Long>();
    private List<UUID> blacklist = new ArrayList<UUID>();

    public MorePackets(Trojan trojan) {
        super("MorePackets", "MorePackets", trojan);
        this.setBannable(false);
        this.setMaxViolations(5);
        this.setEnabled(true);
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event) {
        this.blacklist.add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void PlayerChangedWorld(PlayerChangedWorldEvent event) {
        this.blacklist.add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void PlayerRespawn(PlayerRespawnEvent event) {
        this.blacklist.add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void PacketPlayer(PacketPlayerEvent event) {
        Player player = event.getPlayer();
        if (!this.getTrojan().isEnabled()) {
            return;
        }
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return;
        }
        if (this.getTrojan().lag.getTPS() > 21.0) {
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.packetTicks.containsKey(player.getUniqueId())) {
            Count = this.packetTicks.get(player.getUniqueId()).getKey();
            Time = this.packetTicks.get(player.getUniqueId()).getValue();
        }
        if (this.lastPacket.containsKey(player.getUniqueId())) {
            long MS = System.currentTimeMillis() - this.lastPacket.get(player.getUniqueId());
            if (MS >= 100) {
                this.blacklist.add(player.getUniqueId());
            } else if (MS > 1 && this.blacklist.contains(player.getUniqueId())) {
                this.blacklist.remove(player.getUniqueId());
            }
        }
        if (!this.blacklist.contains(player.getUniqueId())) {
            ++Count;
            if (this.packetTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 1000)) {
                int maxPackets = 49;
                if (Count > maxPackets) {
                    this.getTrojan().logCheat(this, player, "Odd Packets", "??");
                }
                Count = 0;
                Time = UtilTime.nowlong();
            }
        }
        this.packetTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
        this.lastPacket.put(player.getUniqueId(), System.currentTimeMillis());
    }
}

