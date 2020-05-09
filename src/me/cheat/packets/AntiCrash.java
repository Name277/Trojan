package me.cheat.packets;

import java.util.ArrayList;

import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import io.netty.buffer.ByteBuf;

public class AntiCrash extends PacketAdapter {

	ArrayList<String> channels = new ArrayList<String>();
	
	int maxCapacity = 30000;
	
	AntiCrash(Plugin plugin) {
        super(plugin, PacketType.Play.Client.CUSTOM_PAYLOAD);
    	channels.add("MC|BEdit");
    	channels.add("MC|BSign");
    }

	@Override
    public void onPacketReceiving(PacketEvent event) {
        if (channels.contains(event.getPacket().getStrings().getValues().get(0)) && ((ByteBuf) event.getPacket().getModifier().getValues().get(1)).capacity() > maxCapacity) {
            event.setCancelled(true);
     }
    }
}