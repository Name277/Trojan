/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 */
package me.cheat.checks.combat;

import org.bukkit.event.EventHandler;

import me.cheat.Trojan;
import me.cheat.checks.Check;
import me.cheat.packets.events.PacketPlayerEvent;
import me.cheat.packets.events.PacketPlayerType;

public class Twitch
extends Check {
    public Twitch(Trojan trojan) {
        super("Twitch", "Twitch", trojan);
        this.setBannable(true);
        this.setEnabled(true);
        this.setMaxViolations(5);
    }

    @EventHandler
    public void Player(PacketPlayerEvent e) {
        if (e.getType() != PacketPlayerType.LOOK) {
            return;
        }
        if (e.getPitch() > 90.1f || e.getPitch() < -90.1f) {
            this.getTrojan().logCheat(this, e.getPlayer(), "Twitch", new String[0]);
        }
    }
}

