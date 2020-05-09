/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.events.PacketListener
 *  com.comphenix.protocol.reflect.StructureModifier
 *  com.comphenix.protocol.wrappers.EnumWrappers
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityUseAction
 *  net.minecraft.server.v1_7_R4.EnumEntityUseAction
 *  net.minecraft.server.v1_7_R4.PacketPlayInUseEntity
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package me.cheat.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.EnumWrappers;

import me.cheat.Trojan;
import me.cheat.packets.events.PacketBlockPlacementEvent;
import me.cheat.packets.events.PacketEntityActionEvent;
import me.cheat.packets.events.PacketHeldItemChangeEvent;
import me.cheat.packets.events.PacketKeepAliveEvent;
import me.cheat.packets.events.PacketPlayerEvent;
import me.cheat.packets.events.PacketPlayerType;
import me.cheat.packets.events.PacketSwingArmEvent;
import me.cheat.packets.events.PacketUseEntityEvent;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

public class PacketCore {
    public Trojan trojan;

    public PacketCore(Trojan trojan) {
        this.trojan = trojan;
        AntiCrash listener = new AntiCrash(trojan);
        ProtocolLibrary.getProtocolManager().addPacketListener(listener);
        
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this.trojan, new PacketType[]{PacketType.Play.Client.USE_ENTITY}){

            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                if (packet.getHandle() instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity)packet.getHandle() == null)) {
                    return;
                }
                EnumWrappers.EntityUseAction type = (EnumWrappers.EntityUseAction)packet.getEntityUseActions().read(0);
                int entityId = (Integer)packet.getIntegers().read(0);
                Entity entity = null;
                for (Entity entityentity : player.getWorld().getEntities()) {
                    if (entityentity.getEntityId() != entityId) continue;
                    entity = entityentity;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketUseEntityEvent(type, player, entity));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this.trojan, new PacketType[]{PacketType.Play.Client.POSITION_LOOK}){

            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEvent(player, (Double)event.getPacket().getDoubles().read(0), (Double)event.getPacket().getDoubles().read(1), (Double)event.getPacket().getDoubles().read(2), ((Float)event.getPacket().getFloat().read(0)).floatValue(), ((Float)event.getPacket().getFloat().read(1)).floatValue(), PacketPlayerType.POSLOOK));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this.trojan, new PacketType[]{PacketType.Play.Client.LOOK}){

            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEvent(player, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), ((Float)event.getPacket().getFloat().read(0)).floatValue(), ((Float)event.getPacket().getFloat().read(1)).floatValue(), PacketPlayerType.LOOK));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this.trojan, new PacketType[]{PacketType.Play.Client.POSITION}){

            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEvent(player, (Double)event.getPacket().getDoubles().read(0), (Double)event.getPacket().getDoubles().read(1), (Double)event.getPacket().getDoubles().read(2), player.getLocation().getYaw(), player.getLocation().getPitch(), PacketPlayerType.POSITION));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this.trojan, new PacketType[]{PacketType.Play.Client.ENTITY_ACTION}){

            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketEntityActionEvent(player, (Integer)packet.getIntegers().read(1)));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this.trojan, new PacketType[]{PacketType.Play.Client.KEEP_ALIVE}){

            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketKeepAliveEvent(player));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this.trojan, new PacketType[]{PacketType.Play.Client.ARM_ANIMATION}){

            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketSwingArmEvent(event, player));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this.trojan, new PacketType[]{PacketType.Play.Client.HELD_ITEM_SLOT}){

            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketHeldItemChangeEvent(event, player));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this.trojan, new PacketType[]{PacketType.Play.Client.BLOCK_PLACE}){

            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketBlockPlacementEvent(event, player));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this.trojan, new PacketType[]{PacketType.Play.Client.FLYING}){

            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEvent(player, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch(), PacketPlayerType.FLYING));
            }
        });
    }

}

