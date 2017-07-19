package com.github.happyzleaf.magearna;

import com.github.happyzleaf.magearna.util.SpongeUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/***************************************
 * Magearna
 * Created by happyzleaf on 30/05/2017.
 *
 * Copyright (c). All rights reserved.
 ***************************************/
public class MagearnaConfig {
	private MagearnaConfig() {}
	
	private static ConfigurationLoader<CommentedConfigurationNode> loader;
	private static CommentedConfigurationNode node;
	private static File file;
	
	public static String exampleToken = "abcdefghilmnopqrstuvz1234567890-_ABCDEFGHILMOPQRSTUVZ123456";
	public static String exampleId = "123456789012345678";
	
	public static String apiToken = exampleToken;
	public static String serverId = exampleId;
	public static String staffRoomId = exampleId;
	public static String userRoomId = exampleId;
	public static HashMap<String, UUID> discordIdToUUID = Maps.newHashMap();
	
	public static String chatFormat = "&7<DISCORD: %player%> &f%message%";
	
	private static ArrayList<String> blacklist = Lists.newArrayList();
	
	static {
		blacklist.add("lp");
		blacklist.add("pex");
	}
	
	public static void init(ConfigurationLoader<CommentedConfigurationNode> loader, File file) {
		MagearnaConfig.loader = loader;
		MagearnaConfig.file = file;
		loadConfig();
	}
	
	public static void loadConfig() {
		if (!file.exists()) {
			load();
			saveConfig();
		} else {
			load();
			
			CommentedConfigurationNode discord = node.getNode("discord");
			apiToken = discord.getNode("apiToken").getString();
			serverId = discord.getNode("serverId").getString();
			staffRoomId = discord.getNode("staffRoomId").getString();
			userRoomId = discord.getNode("userRoomId").getString();
			
			CommentedConfigurationNode minecraft = node.getNode("minecraft");
			CommentedConfigurationNode mchat = minecraft.getNode("chat");
			chatFormat = mchat.getNode("format").getString();
			
			discordIdToUUID.clear();
			HashMap<String, String> tempMap = (HashMap<String, String>) node.getNode("users").getValue();
			for (String key : tempMap.keySet()) {
				discordIdToUUID.put(key, UUID.fromString(tempMap.get(key)));
			}
			
			blacklist = (ArrayList<String>) node.getNode("blacklist").getValue();
			
			save();
		}
	}
	
	public static void saveConfig() {
		CommentedConfigurationNode discord = node.getNode("discord");
		discord.getNode("apiToken").setComment("https://discordapp.com/developers/applications/me").setValue(apiToken);
		discord.getNode("serverId").setValue(serverId);
		discord.getNode("staffRoomId").setValue(staffRoomId);
		discord.getNode("userRoomId").setValue(userRoomId);
		
		HashMap<String, String> tempMap = Maps.newHashMap();
		for (String key : discordIdToUUID.keySet()) {
			tempMap.put(key, discordIdToUUID.get(key).toString());
		}
		node.getNode("users").setValue(tempMap);
		
		node.getNode("blacklist").setComment("You need only one alias per plugin. Examples: pex, lp.").setValue(blacklist);
		
		CommentedConfigurationNode minecraft = node.getNode("minecraft");
		CommentedConfigurationNode mchat = minecraft.getNode("chat").setComment("You can use colors and %player%");
		mchat.getNode("format").setComment("You can %message%").setValue(chatFormat);
		
		save();
		load();
	}
	
	public static boolean isBlacklisted(String command) {
		return blacklist.stream().anyMatch(s -> SpongeUtil.getAllAlias(s).contains(command));
	}
	
	private static void load() {
		try {
			node = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void save() {
		try {
			loader.save(node);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
