package me.cheat.checks.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import me.cheat.Trojan;
import me.cheat.checks.Check;
import me.cheat.packets.events.PacketPlayerEvent;
import me.cheat.util.UtilMath;

public class Timer extends Check {
	
  public Timer(Trojan trojan) {
	  	super("Timer", "Timer", trojan);
    	this.setBannable(true);
    	this.setEnabled(true);
  }
  
  private Map<UUID, Long> lastTimer = new HashMap();
  private Map<UUID, List<Long>> MS = new HashMap();
  private Map<UUID, Integer> timerTicks = new HashMap();
  
  @EventHandler
  public void PacketPlayer(PacketPlayerEvent event) {
    Player player = event.getPlayer();
    int Count = 0;
    if (this.timerTicks.containsKey(player.getUniqueId())) {
      Count = ((Integer)this.timerTicks.get(player.getUniqueId())).intValue();
    }
    if (this.lastTimer.containsKey(player.getUniqueId()))
    {
      long MS = System.currentTimeMillis() - ((Long)this.lastTimer.get(player.getUniqueId())).longValue();
      
      List<Long> List = new ArrayList();
      if (this.MS.containsKey(player.getUniqueId())) {
        List = (List)this.MS.get(player.getUniqueId());
      }
      List.add(Long.valueOf(MS));
      if (List.size() == 20)
      {
        boolean doeet = true;
        for (Long ListMS : List) {
          if (ListMS.longValue() < 1L) {
            doeet = false;
          }
        }
        Long average = Long.valueOf(UtilMath.averageLong(List));
        if ((average.longValue() < 48L) && (doeet))
        {
          Count++;
        }
        else
        {
          Count = 0;
        }
        this.MS.remove(player.getUniqueId());
      }
      else
      {
        this.MS.put(player.getUniqueId(), List);
      }
    }
    if (Count > 4)
    {
      Count = 0;
      
      getTrojan().logCheat(this, player, "Timer Speed", new String[0]);
    }
    this.lastTimer.put(player.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
    this.timerTicks.put(player.getUniqueId(), Integer.valueOf(Count));
  }
}
