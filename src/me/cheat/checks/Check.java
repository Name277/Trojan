package me.cheat.checks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import me.cheat.Trojan;

public class Check implements Listener {
	
    private String Identifier;
    private String Name;
    private Trojan Trojan;
    private boolean Enabled = true;
    private boolean BanTimer = false;
    private boolean Bannable = true;
    private Integer MaxViolations = 5;
    private Integer ViolationsToNotify = 1;
    private Long ViolationResetTime = (long) 600000;
    public Map<String, List<String>> DumpLogs = new HashMap<String, List<String>>();

    public Check(String Identifier, String Name, Trojan trojan) {
        this.Name = Name;
        this.Trojan = trojan;
        this.Identifier = Identifier;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public boolean isEnabled() {
        return this.Enabled;
    }

    public boolean isBannable() {
        return this.Bannable;
    }

    public boolean hasBanTimer() {
        return this.BanTimer;
    }

    public Trojan getTrojan() {
        return this.Trojan;
    }

    public Integer getMaxViolations() {
        return this.MaxViolations;
    }

    public Integer getViolationsToNotify() {
        return this.ViolationsToNotify;
    }

    public Long getViolationResetTime() {
        return this.ViolationResetTime;
    }

    public void setEnabled(boolean Enabled) {
        if (Enabled) {
            if (!this.isEnabled()) {
                this.Trojan.RegisterListener(this);
            }
        } else if (this.isEnabled()) {
            HandlerList.unregisterAll((Listener)this);
        }
        this.Enabled = Enabled;
    }

    public void setBannable(boolean Bannable) {
        this.Bannable = Bannable;
    }

    public void setAutobanTimer(boolean BanTimer) {
        this.BanTimer = BanTimer;
    }

    public void setMaxViolations(int MaxViolations) {
        this.MaxViolations = MaxViolations;
    }

    public void setViolationsToNotify(int ViolationsToNotify) {
        this.ViolationsToNotify = ViolationsToNotify;
    }

    public void setViolationResetTime(long ViolationResetTime) {
        this.ViolationResetTime = ViolationResetTime;
    }

    public String getName() {
        return this.Name;
    }

    public String getIdentifier() {
        return this.Identifier;
    }
}

