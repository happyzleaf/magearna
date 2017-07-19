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
public class ReloadBotCommand implements CommandExecutor {
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Magearna.reloadBot();
		src.sendMessage(Text.of(TextColors.GREEN, "[" + Magearna.PLUGIN_NAME + "] Discord bot successfully reloaded."));
		return CommandResult.success();
	}
}
