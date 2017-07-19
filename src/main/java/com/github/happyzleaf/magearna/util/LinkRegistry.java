package com.github.happyzleaf.magearna.util;

import com.google.common.collect.Maps;
import net.dv8tion.jda.core.entities.User;
import org.spongepowered.api.Sponge;

import java.util.HashMap;
import java.util.Random;

/***************************************
 * Magearna
 * Created by happyzleaf on 03/06/2017.
 *
 * Copyright (c). All rights reserved.
 ***************************************/
public class LinkRegistry {
	private static HashMap<String, User> playerNameToLinkKey = Maps.newHashMap();
	private static Random random = new Random(Sponge.getServer().getDefaultWorld().get().getSeed() + System.currentTimeMillis());
	
	public static String newKey(User player) {
		String key;
		do {
			key = random.nextInt(10) + "" + random.nextInt(10) + "" + random.nextInt(10) + "" + random.nextInt(10) + "" + random.nextInt(10) + "-" + random.nextInt(10) + "" + random.nextInt(10) + "" + random.nextInt(10) + "" + random.nextInt(10) + "" + random.nextInt(10);
		} while (playerNameToLinkKey.keySet().contains(key));
		playerNameToLinkKey.put(key, player);
		return key;
	}
	
	public static User getUser(String key) {
		return playerNameToLinkKey.get(key);
	}
}
