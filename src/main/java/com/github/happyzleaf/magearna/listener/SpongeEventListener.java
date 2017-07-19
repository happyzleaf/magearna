package com.github.happyzleaf.magearna.listener;

import com.github.happyzleaf.magearna.util.DiscordUtil;
import net.dv8tion.jda.core.entities.TextChannel;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.channel.MessageChannel;

import javax.annotation.Nullable;
import java.util.Optional;

/***************************************
 * Magearna
 * Created by happyzleaf on 03/06/2017.
 *
 * Copyright (c). All rights reserved.
 ***************************************/
public class SpongeEventListener {
	@Listener
	public void onChat(MessageChannelEvent event) {
		Optional<MessageChannel> optChannel = event.getChannel();
		Optional<CommandSource> optSource = event.getCause().first(CommandSource.class);
		System.out.println("message fired");
		if (optChannel.isPresent() && optSource.isPresent()) {
			MessageChannel channel = optChannel.get();
			CommandSource source = optSource.get();
			System.out.println("source is present");
			System.out.println("source is " + source.getClass().getName());
			TextChannel textChannel = null;
			if (channel.getClass().getName().equals("io.github.nucleuspowered.nucleus.modules.staffchat.StaffChatMessageChannel")) {
				DiscordUtil.getStaffChannel().sendMessage("`" + source.getName() + ":` " + event.getMessage().toPlain()).queue();
			} else if (channel.getClass().getName().startsWith("org.spongepowered.api.text.channel.MessageChannel")) {
				DiscordUtil.getUserChannel().sendMessage(event.getMessage().toPlain().replaceFirst(source.getName(), "`" + source.getName() + "`")).queue();
			}
		}
	}
	
	private static void sendFormattedMessage(MessageChannelEvent event, @Nullable Player player, TextChannel channel) {
		channel.sendMessage((player == null ? "" : "`" + player.getName() + ":` ") + event.getMessage().toPlain()).queue();
	}
}
