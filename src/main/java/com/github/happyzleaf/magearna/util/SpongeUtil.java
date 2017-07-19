package com.github.happyzleaf.magearna.util;

import com.github.happyzleaf.magearna.Magearna;
import com.google.common.collect.Sets;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.io.File;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/***************************************
 * Magearna
 * Created by happyzleaf on 03/06/2017.
 *
 * Copyright (c). All rights reserved.
 ***************************************/
public class SpongeUtil {
	public static User getUser(UUID uuid) {
		Optional<Player> onlinePlayer = Sponge.getServer().getPlayer(uuid);
		if (onlinePlayer.isPresent()) {
			return onlinePlayer.get();
		}
		return Sponge.getServiceManager().provide(UserStorageService.class).get().get(uuid).orElse(null);
	}
	
	public static User getUser(String lastKnownNick) {
		Optional<Player> onlinePlayer = Sponge.getServer().getOnlinePlayers().stream().filter(player -> player.getName().equals(lastKnownNick)).findFirst();
		if (onlinePlayer.isPresent()) {
			return onlinePlayer.get();
		}
		return Sponge.getServiceManager().provide(UserStorageService.class).get().get(lastKnownNick).orElse(null);
	}
	
	public static Set<String> getAllAlias(String command) {
		Set<String> set = Sets.newHashSet();
		Optional<? extends CommandMapping> optCommand = Sponge.getCommandManager().get(command);
		if (optCommand.isPresent()) {
			set = optCommand.get().getAllAliases();
		}
		return set;
	}
}
