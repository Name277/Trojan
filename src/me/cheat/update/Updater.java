/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 */
package me.cheat.update;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import me.cheat.Trojan;
import me.cheat.update.UpdateType;
import me.cheat.update.events.UpdateEvent;

public class Updater
implements Runnable {
    private Trojan trojan;
    private int updater;

    public Updater(Trojan trojan) {
        this.trojan = trojan;
        this.updater = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this.trojan, (Runnable)this, 0, 1);
    }

    public void Disable() {
        Bukkit.getScheduler().cancelTask(this.updater);
    }

    @Override
    public void run() {
        UpdateType[] arrupdateType = UpdateType.values();
        int n = arrupdateType.length;
        int n2 = 0;
        while (n2 < n) {
            UpdateType updateType = arrupdateType[n2];
            if (updateType != null && updateType.Elapsed()) {
                try {
                    UpdateEvent event = new UpdateEvent(updateType);
                    Bukkit.getPluginManager().callEvent((Event)event);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            ++n2;
        }
    }
}

