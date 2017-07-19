package com.github.happyzleaf.magearna.util;

import com.github.happyzleaf.magearna.MagearnaConfig;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.util.Tristate;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/***************************************
 * Magearna
 * Created by happyzleaf on 03/06/2017.
 *
 * Copyright (c). All rights reserved.
 ***************************************/
public class DiscordCommandSource implements ConsoleSource {
	private ConsoleSource console;
	private User player;
	public String messages = "";
	
	public DiscordCommandSource(String userId) {
		console = Sponge.getServer().getConsole();
		player = SpongeUtil.getUser(MagearnaConfig.discordIdToUUID.get(userId));
	}
	
	@Override
	public void sendMessage(Text message) {
		console.sendMessage(message);
		messages += message.toPlain() + "\n";
	}
	
	@Override
	public boolean hasPermission(String permission) {
		return player.hasPermission(permission);
	}
	
	@Override
	public boolean hasPermission(Set<Context> contexts, String permission) {
		return player.hasPermission(contexts, permission);
	}
	
	@Override
	public String getName() {
		return player.getName();
	}
	
	@Override
	public Optional<CommandSource> getCommandSource() {
		return console.getCommandSource();
	}
	
	@Override
	public SubjectCollection getContainingCollection() {
		return console.getContainingCollection();
	}
	
	@Override
	public SubjectData getSubjectData() {
		return console.getSubjectData();
	}
	
	@Override
	public SubjectData getTransientSubjectData() {
		return console.getTransientSubjectData();
	}
	
	@Override
	public Tristate getPermissionValue(Set<Context> contexts, String permission) {
		return console.getPermissionValue(contexts, permission);
	}
	
	@Override
	public boolean isChildOf(Set<Context> contexts, Subject parent) {
		return console.isChildOf(contexts, parent);
	}
	
	@Override
	public List<Subject> getParents(Set<Context> contexts) {
		return console.getParents(contexts);
	}
	
	@Override
	public Optional<String> getOption(Set<Context> contexts, String key) {
		return console.getOption(contexts, key);
	}
	
	@Override
	public String getIdentifier() {
		return console.getIdentifier();
	}
	
	@Override
	public Set<Context> getActiveContexts() {
		return console.getActiveContexts();
	}
	
	@Override
	public MessageChannel getMessageChannel() {
		return console.getMessageChannel();
	}
	
	@Override
	public void setMessageChannel(MessageChannel channel) {
		console.setMessageChannel(channel);
	}
}
