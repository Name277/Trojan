package me.cheat.lag;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import me.cheat.Trojan;

public class LagCore
implements Listener {
	
    public Trojan trojan;
    private double tps;

    public LagCore(Trojan trojan) {
        this.trojan = trojan;
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this.trojan, new Runnable(){
            long sec;
            long currentSec;
            int ticks;

            @Override
            public void run() {
                this.sec = System.currentTimeMillis() / 1000;
                if (this.currentSec == this.sec) {
                    ++this.ticks;
                } else {
                    this.currentSec = this.sec;
                    LagCore.access$1(LagCore.this, LagCore.this.tps == 0.0 ? (double)this.ticks : (LagCore.this.tps + (double)this.ticks) / 2.0);
                    this.ticks = 0;
                }
            }
        }, 0, 1);
        this.trojan.RegisterListener(this);
    }

    public double getTPS() {
        return this.tps + 1.0 > 20.0 ? 20.0 : this.tps + 1.0;
    }

    public int getPing(Player player) {
        CraftPlayer cp = (CraftPlayer)player;
        EntityPlayer ep = cp.getHandle();
        return ep.ping;
    }

    static void access$1(LagCore lagCore, double d) {
        lagCore.tps = d;
    }

}

