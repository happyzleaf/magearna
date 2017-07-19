package com.github.happyzleaf.magearna;

import com.github.happyzleaf.magearna.command.LinkCommand;
import com.github.happyzleaf.magearna.command.ReloadBotCommand;
import com.github.happyzleaf.magearna.command.ShutdownBotCommand;
import com.github.happyzleaf.magearna.command.StartBotCommand;
import com.github.happyzleaf.magearna.listener.DiscordEventListener;
import com.github.happyzleaf.magearna.listener.SpongeEventListener;
import com.github.happyzleaf.magearna.util.DiscordUtil;
import com.github.happyzleaf.magearna.util.SpongeUtil;
import com.google.inject.Inject;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;

/***************************************
 * Magearna
 * Created by happyzleaf on 30/05/2017.
 *
 * Copyright (c). All rights reserved.
 ***************************************/
@Plugin(id = Magearna.PLUGIN_ID, name = Magearna.PLUGIN_NAME, version = Magearna.VERSION, authors = {"happyzleaf"},
		description = "Magearna is a discord bot that allows server admins to execute in-game commands and to be notified when something happens in their server.",
		url = "http://pokegeared.enjin.com/")
public class Magearna {
	public static final String PLUGIN_ID = "magearna";
	public static final String PLUGIN_NAME = "Magearna";
	public static final String VERSION = "1.0.2";
	
	public static final Logger LOGGER = LoggerFactory.getLogger(PLUGIN_NAME);
	
	public static Magearna instance;
	private static SpongeEventListener spongeListener = new SpongeEventListener();
	public static JDA jda;
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> configLoader;
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	private File configFile;
	
	@Listener
	public void preInit(GamePreInitializationEvent event) {
		instance = this;
		MagearnaConfig.init(configLoader, configFile);
	}
	
	@Listener
	public void onServerStarted(GameStartedServerEvent event) {
		CommandSpec start = CommandSpec.builder()
				.permission(PLUGIN_ID + ".command.bot.start")
				.executor(new StartBotCommand())
				.build();
		CommandSpec shutdown = CommandSpec.builder()
				.permission(PLUGIN_ID + ".command.bot.shutdown")
				.executor(new ShutdownBotCommand())
				.build();
		CommandSpec reload = CommandSpec.builder()
				.permission(PLUGIN_ID + ".command.bot.reload")
				.executor(new ReloadBotCommand())
				.build();
		CommandSpec bot = CommandSpec.builder()
				.child(start, "start")
				.child(shutdown, "shutdown")
				.child(reload, "reload")
				.build();
		CommandSpec link = CommandSpec.builder()
				.arguments(GenericArguments.string(Text.of("key")))
				.permission(PLUGIN_ID + ".command.link")
				.executor(new LinkCommand())
				.build();
		CommandSpec mainCommand = CommandSpec.builder()
				.executor((src, args) -> {
					src.sendMessage(Text.of(TextColors.AQUA, PLUGIN_NAME + " is a discord utility for server admins created by happyzleaf!"));
					return CommandResult.success();
				})
				.child(bot, "bot")
				.child(link, "link")
				.build();
		Sponge.getCommandManager().register(this, mainCommand, PLUGIN_ID);
		startBot();
	}
	
	@Listener
	public void onReload(GameReloadEvent event) {
		MagearnaConfig.loadConfig();
	}
	
	/*@Listener
	public void onServerStopping(GameStoppingServerEvent event) {
		if (jda != null) {
			DiscordUtil.getStaffChannel().sendMessage("Magearna is being disconnected. :cry:").queue();
			stopBot();
		}
	}*/
	
	/**
	 * @return If the bot has started
	 */
	public static boolean startBot() {
		if (MagearnaConfig.apiToken.equals(MagearnaConfig.exampleToken)
				|| MagearnaConfig.serverId.equals(MagearnaConfig.exampleId)
				|| MagearnaConfig.staffRoomId.equals(MagearnaConfig.exampleId)
				|| MagearnaConfig.userRoomId.endsWith(MagearnaConfig.exampleId)) {
			LOGGER.info("Please setup the bot in " + PLUGIN_ID + ".conf!");
			return false;
		}
		
		Sponge.getEventManager().registerListeners(Magearna.instance, spongeListener);
		
		LOGGER.info("Launching discord bot.");
		try {
			jda = new JDABuilder(AccountType.BOT)
					.setToken(MagearnaConfig.apiToken)
					.addEventListener(new DiscordEventListener())
					.setGame(Game.of("with screws and bolts", "https://pokegeared.com/"))
					.buildBlocking();
		} catch (LoginException | RateLimitedException | InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		DiscordUtil.getStaffChannel().sendMessage(Magearna.PLUGIN_NAME + " up and running! " + DiscordUtil.getGuild().getEmotesByName("victory", true).get(0).getAsMention()).complete();
		/*try {
			Message message = DiscordUtil.getStaffChannel().sendFile(SpongeUtil.getAssetFile("miranda.jpg"), null).complete();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return true;
	}
	
	public static void stopBot() {
		Sponge.getEventManager().unregisterListeners(spongeListener);
		jda.shutdown();
		jda = null;
	}
	
	public static void reloadBot() {
		if (jda != null) {
			stopBot();
		}
		startBot();
	}
}
