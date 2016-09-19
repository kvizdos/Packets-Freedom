/*
 * 
 * Packets - Created By Kenton Vizdos - @Kvizdos On Twitter
 * Contact Me At Kvizdos@gmail.com for reuse. 
 * ALL rights reserved, no copying
 * 
 */

package com.Kento.PCM;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_9_R1.EntityPlayer;

public class Main extends JavaPlugin { // If you dont know what this means than.. huh..

	private List<WrappedGameProfile> message = new ArrayList<WrappedGameProfile>(); // Creates a WrappedGameProfile list, which is for all metadata

	public void onEnable() { // Enables :)
		
		saveDefaultConfig();
		
		if(getConfig().getInt("Max") == 0) {
			getConfig().set("Max", Bukkit.getMaxPlayers());
		}
		
		if (!new File(getDataFolder(), "RESET.FILE").exists()) { // If needed file exists:
			try {
				getConfig().set("PlayerCountMessage", Arrays.asList("&6Packets", "&5Created By Kento", "&b@kvizdos on Twitter!")); // THIS IS WHERE THE PLAYER COUNT MESSAGE GOES! EACH LIST ITEM IS A NEW LINE! *NOTE* IN THIS FREE VERSION, YOU CAN ONLY HAVE 3 LIST ITEMS, I THINK YOU CAN FIGURE OUT HOW TO REMOVE THAT!

				new File(getDataFolder(), "RESET.FILE").createNewFile(); // Creates file :)
			} catch (IOException e) {
				Bukkit.getConsoleSender().sendMessage(pr + "Creating Files..");
			}
		}
		saveConfig();
		for (String str : getConfig().getStringList("PlayerCountMessage")) // Gets the config stringlist and for each item, put it into the String str;
			message.add(new WrappedGameProfile("1", ChatColor.translateAlternateColorCodes('&', str.toString()))); // Translate color codes *&6Hello!* Would be golden

		List<String> motds = getConfig().getStringList("PlayerCountMessage");
		
		ProtocolLibrary.getProtocolManager() // PROTOCOL LiB startup
				.addPacketListener(
					new PacketAdapter(this, ListenerPriority.NORMAL,
						Arrays.asList(new PacketType[] { PacketType.Status.Server.OUT_SERVER_INFO }),
						new ListenerOptions[] { ListenerOptions.ASYNC }) {
					
					public void onPacketSending(PacketEvent e) {
						e.getPacket().getServerPings().read(0).setMotD(ChatColor.translateAlternateColorCodes('&', getConfig().getString("MOTD")));
						// TO HERE -------------------------^ IS NECCESSITY! AFTER THIS THERE IS SO MUCH STUFF TO ADD! repeats

							e.getPacket().getServerPings().read(0).setPlayers(Main.this.message);
							
							int online = getConfig().getInt("Online");
							int max = getConfig().getInt("Max");

							e.getPacket().getServerPings().read(0).setPlayersOnline(online);
							
							e.getPacket().getServerPings().read(0).setPlayersMaximum(max);

							e.getPacket().getServerPings().read(0).setVersionName(getConfig().getString("Version"));

					}

				});
		
	}

	String pr = ChatColor.GOLD + "Packets: ";
	private EntityPlayer npc;
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player) sender;
		
		String mess = "";
		for(int i = 1; i < args.length; i++) {
			String arg = args[i] + " ";
			mess = mess + arg;
		}
		
		if(cmd.getName().equalsIgnoreCase("packets")) {
			if(args.length == 0) {
				p.sendMessage(ChatColor.GOLD + "Packets" + ChatColor.GRAY + " - " + ChatColor.DARK_PURPLE + "Created By Kento" + ChatColor.GRAY + " - " + ChatColor.AQUA + "@Kvizdos On Twitter" + ChatColor.GRAY + " - " + ChatColor.RED + "YouTube.Com/TheRealmOfTheGeek");
				p.sendMessage(pr + "Do '/packets help' for help!");
				return true;
			} else if(args[0].equalsIgnoreCase("help")) {
				p.sendMessage(pr + "/packets motd <motd>");
				p.sendMessage(pr + "/packets spoof <max/online> <#>");
				p.sendMessage(pr + "/packets spoof version <text>");
				p.sendMessage(pr + "*Player Count Message Must Be Edited In Config*");
				return true;
			} else if(args[0].equalsIgnoreCase("motd") && p.hasPermission("Packets.MOTD")) {
				if(args.length == 1) {
					p.sendMessage(pr + "Do /packets motd <motd>");
					return true;
				} else {
					p.sendMessage(pr + "Set: " + mess + "as the MOTD!");
					getConfig().set("MOTD", mess);
					saveConfig();
					reloadConfig();
					return true;
				}
			} else if(args[0].equalsIgnoreCase("spoof") && p.hasPermission("Packets.spoof")) {
				if(args.length == 1) {
					p.sendMessage(pr + "Please do /packets spoof <max/online> <#> OR /packets spoof version <text>");
					return true;
				} else if(args[1].equalsIgnoreCase("online")) {
					if(args.length == 2) {
						p.sendMessage(pr + "Please do /packets spoof online <#>");
						return true;
					} else {

						if(Integer.parseInt(args[2]) > getConfig().getInt("Max")) {
							p.sendMessage(ChatColor.RED + "WARNING - ONLINE PLAYERS SET TO MORE THAN MAX PLAYER COUNT - WARNING");
							p.sendMessage(pr + "Set online player count to " + args[2]);
							getConfig().set("Online", Integer.parseInt(args[2]));
							saveConfig();
							reloadConfig();
							return true;
						} else {
							p.sendMessage(pr + "Set online player count to " + args[2]);
							getConfig().set("Online", Integer.parseInt(args[2]));
							saveConfig();
							reloadConfig();
							return true;
						}
						
					}
				} else if(args[1].equalsIgnoreCase("max")) {
					if(args.length == 2) {
						p.sendMessage(pr + "Please do /packets spoof max <#>");
						return true;
					} else {
						p.sendMessage(pr + "Set max player count to " + args[2]);
						getConfig().set("Max", Integer.parseInt(args[2]));
						saveConfig();
						reloadConfig();
						return true;
					}
				} if(args.length == 2) {
					p.sendMessage(pr + "Please do /packets spoof version <text>");
					return true;
				} else {
					String messs = "";
					for(int i = 2; i < args.length; i++) {
						String arg = args[i] + " ";
						messs = messs + arg;
					}
					p.sendMessage(pr + "Set Version Message to " + messs);
					getConfig().set("Version", messs);
					saveConfig();
					reloadConfig();
					return true;
				}
			}
		}
		return false;
	}
}
