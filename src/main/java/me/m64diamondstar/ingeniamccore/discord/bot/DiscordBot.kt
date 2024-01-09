package me.m64diamondstar.ingeniamccore.discord.bot

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.discord.commands.admin.EmbedCommand
import me.m64diamondstar.ingeniamccore.discord.commands.linking.LinkCommand
import me.m64diamondstar.ingeniamccore.discord.commands.logs.LogsCommand
import me.m64diamondstar.ingeniamccore.discord.commands.minecraft.ChatCommand
import me.m64diamondstar.ingeniamccore.discord.commands.profile.ProfileCommand
import me.m64diamondstar.ingeniamccore.discord.commands.tickets.CloseCommand
import me.m64diamondstar.ingeniamccore.discord.commands.tickets.TicketCommand
import me.m64diamondstar.ingeniamccore.discord.commands.utils.IpCommand
import me.m64diamondstar.ingeniamccore.discord.commands.utils.WhatismyidCommand
import me.m64diamondstar.ingeniamccore.discord.listeners.DiscordChatListener
import me.m64diamondstar.ingeniamccore.discord.listeners.GuildReadyListener
import me.m64diamondstar.ingeniamccore.discord.listeners.LogsListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag

object DiscordBot {

    lateinit var jda: JDA

    fun start() {
        if(!IngeniaMC.plugin.config.getBoolean("Discord.Webhook.Enable"))
            return
        if(IngeniaMC.plugin.config.getString("Discord.Bot.Token") == null)
            return

        jda = JDABuilder.createDefault(IngeniaMC.plugin.config.getString("Discord.Bot.Token"))
            .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES,GatewayIntent.MESSAGE_CONTENT)
            .addEventListeners(GuildReadyListener(), EmbedCommand(), TicketCommand(), LogsCommand(),
                CloseCommand(), WhatismyidCommand(), LinkCommand(), ProfileCommand(), IpCommand(),
                LogsListener(), ChatCommand(), DiscordChatListener()
            )
            .enableCache(CacheFlag.EMOJI)
            .setActivity(Activity.playing("play.IngeniaMC.net on 1.20.1"))
            .build()
    }

    fun shutdown(){
        if(!IngeniaMC.plugin.config.getBoolean("Discord.Webhook.Enable"))
            return
        jda.shutdown()
    }

}