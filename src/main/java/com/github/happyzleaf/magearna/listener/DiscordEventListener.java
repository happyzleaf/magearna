package com.github.happyzleaf.magearna.listener;

import com.github.happyzleaf.magearna.Magearna;
import com.github.happyzleaf.magearna.MagearnaConfig;
import com.github.happyzleaf.magearna.util.DiscordCommandSource;
import com.github.happyzleaf.magearna.util.LinkRegistry;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.spongepowered.api.Sponge;
import net.dv8tion.jda.core.entities.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

/***************************************
 * Magearna
 * Created by happyzleaf on 31/05/2017.
 *
 * Copyright (c). All rights reserved.
 ***************************************/
public class DiscordEventListener extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		TextChannel channel = event.getTextChannel();
		if (event.getPrivateChannel() == null && event.getGuild().getId().equals(MagearnaConfig.serverId) ) {
			User author = event.getAuthor();
			String message = event.getMessage().getRawContent();
			if (!event.getAuthor().isBot()) {
				if (channel.getId().equals(MagearnaConfig.staffRoomId)) {
					if (message.startsWith("/")) {
						if (MagearnaConfig.discordIdToUUID.containsKey(author.getId())) {
							String command = message.substring(1);
							if (MagearnaConfig.isBlacklisted(command)) {
								channel.sendMessage("The command `" + command + "` is blacklisted.").queue();
							} else {
								DiscordCommandSource source = new DiscordCommandSource(author.getId());
								Sponge.getCommandManager().process(source, command);
								if (!source.messages.isEmpty()) {
									channel.sendMessage("```css\n" + source.messages + "```").queue();
								}
							}
						} else {
							channel.sendMessage("You need to link your account to Minecraft, please type `!link`.").queue();
						}
					} else if (message.startsWith("!link")) {
						author.openPrivateChannel().complete().sendMessage("Please type `/" + Magearna.PLUGIN_ID + " link " + LinkRegistry.newKey(author) + "` in the Minecraft server.").queue();
						channel.sendMessage(author.getName() + " a key was sent to your private channel.").queue();
					} else {
						Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "a [" + author.getName() + "] " + message);
					}
				} else if (channel.getId().equals(MagearnaConfig.userRoomId)) {
					if (message.startsWith("/")) {
						channel.sendMessage(author.getName() + " you can't execute commands in this room.").queue();
					} else if (!message.isEmpty()) {
						MessageChannel.TO_PLAYERS.send(Text.of(TextSerializers.FORMATTING_CODE.deserialize(MagearnaConfig.chatFormat
								.replaceAll("%player%", author.getName())
								.replaceAll("%message%", message)
						)));
					}
				}
			} else if (message.startsWith(Magearna.jda.getSelfUser().getAsMention())) {
				channel.sendMessage("Yessir?").queue();
			}
		}
	}
}
