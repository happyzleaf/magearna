package com.github.happyzleaf.magearna.util;

import com.github.happyzleaf.magearna.Magearna;
import com.github.happyzleaf.magearna.MagearnaConfig;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

/***************************************
 * Magearna
 * Created on 19/06/2017.
 * @author Vincenzo Montanari
 *
 * Copyright (c). All rights reserved.
 ***************************************/
public class DiscordUtil {
	public static Guild getGuild() {
		return Magearna.jda.getGuildById(MagearnaConfig.serverId);
	}
	
	public static TextChannel getStaffChannel() {
		return getGuild().getTextChannelById(MagearnaConfig.staffRoomId);
	}
	
	public static TextChannel getUserChannel() {
		return getGuild().getTextChannelById(MagearnaConfig.userRoomId);
	}
}
