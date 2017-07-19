package com.github.happyzleaf.magearna.command;

import com.github.happyzleaf.magearna.Magearna;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/***************************************
 * Magearna
 * Created on 19/06/2017.
 * @author Vincenzo Montanari
 *
 * Copyright (c). All rights reserved.
 ***************************************/
public class StartBotCommand implements CommandExecutor {
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (Magearna.jda == null) {
			src.sendMessage(Text.of(TextColors.RED, "[" + Magearna.PLUGIN_NAME + "] The discord bot is already running, you can reload it with ", TextColors.DARK_RED, "/" + Magearna.PLUGIN_ID + " bot reload", TextColors.RED, "."));
		} else {
			if (Magearna.startBot()) {
				src.sendMessage(Text.of(TextColors.GREEN, "[" + Magearna.PLUGIN_NAME + "] Discord bot successfully started."));
				return CommandResult.success();
			} else {
				src.sendMessage(Text.of(TextColors.RED, "[" + Magearna.PLUGIN_NAME + "] There was a problem while starting the bot."));
			}
		}
		return CommandResult.successCount(0);
	}
}
