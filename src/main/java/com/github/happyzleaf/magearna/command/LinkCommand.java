package com.github.happyzleaf.magearna.command;

import com.github.happyzleaf.magearna.Magearna;
import com.github.happyzleaf.magearna.MagearnaConfig;
import com.github.happyzleaf.magearna.util.LinkRegistry;
import net.dv8tion.jda.core.entities.User;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

/***************************************
 * Magearna
 * Created by happyzleaf on 03/06/2017.
 *
 * Copyright (c). All rights reserved.
 ***************************************/
public class LinkCommand implements CommandExecutor {
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Optional<String> optKey = args.getOne("key");
		if (src instanceof Player) {
			if (optKey.isPresent()) {
				Player player = (Player) src;
				User user = LinkRegistry.getUser(optKey.get());
				if (user == null) {
					src.sendMessage(Text.of(TextColors.RED, "[" + Magearna.PLUGIN_NAME + "] The link key is invalid!"));
				} else {
					MagearnaConfig.discordIdToUUID.put(user.getId(), player.getUniqueId());
					MagearnaConfig.saveConfig();
					src.sendMessage(Text.of(TextColors.GREEN, "[" + Magearna.PLUGIN_NAME + "] Successfully registered " + user.getName() + " to your minecraft account!"));
					return CommandResult.success();
				}
			}
		} else {
			src.sendMessage(Text.of(TextColors.RED, "[" + Magearna.PLUGIN_NAME + "] You need to be in-game in order to execute this command!"));
		}
		return CommandResult.successCount(0);
	}
}
