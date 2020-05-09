package me.cheat.checks.combat;

import com.comphenix.protocol.wrappers.EnumWrappers;

import me.cheat.Trojan;
import me.cheat.checks.Check;
import me.cheat.packets.events.PacketUseEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class DoubleClick
extends Check {
    private Map<UUID, Long[]> LastMSCPS = new HashMap<UUID, Long[]>();

    public DoubleClick(Trojan trojan) {
        super("DoubleClick", "DoubleClick", trojan);
        this.setBannable(false);
        this.setAutobanTimer(false);
        this.setEnabled(true);
    }

    @Override
    public void onEnable() {
    }

    @EventHandler
    public void UseEntity(PacketUseEntityEvent e) {
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK) {
            return;
        }
        if (!(e.getAttacked() instanceof Player)) {
            return;
        }
        Player damager = e.getAttacker();
        Long first = 0L;
        Long second =  0L;
        if (this.LastMSCPS.containsKey(damager.getUniqueId())) {
            first = this.LastMSCPS.get(damager.getUniqueId())[0];
            second = this.LastMSCPS.get(damager.getUniqueId())[1];
        }
        if (first == 0) {
            first = System.currentTimeMillis();
        } else if (second == 0) {
            second = System.currentTimeMillis();
            first = System.currentTimeMillis() - first;
        } else {
            second = System.currentTimeMillis() - second;
            if (first > 50 && second == 0) {
                this.getTrojan().logCheat(this, damager, null, new String[0]);
            }
            first = 0L;
            second = 0L;
        }
        this.LastMSCPS.put(damager.getUniqueId(), new Long[]{first, second});
    }
}

