package me.m64diamondstar.ingeniamccore.discord.bot

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.discord.commands.admin.EmbedCommand
import me.m64diamondstar.ingeniamccore.discord.commands.`fun`.MemeCommand
import me.m64diamondstar.ingeniamccore.discord.commands.linking.LinkCommand
import me.m64diamondstar.ingeniamccore.discord.commands.profile.ProfileCommand
import me.m64diamondstar.ingeniamccore.discord.commands.tickets.CloseCommand
import me.m64diamondstar.ingeniamccore.discord.commands.tickets.TicketCommand
import me.m64diamondstar.ingeniamccore.discord.commands.utils.IpCommand
import me.m64diamondstar.ingeniamccore.discord.commands.utils.WhatismyidCommand
import me.m64diamondstar.ingeniamccore.discord.listeners.GuildReadyListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent

object DiscordBot {

    lateinit var jda: JDA

    fun start() {
        jda = JDABuilder.createDefault(IngeniaMC.plugin.config.getString("Discord.Bot.Token"))
            .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES)
            .addEventListeners(GuildReadyListener(), MemeCommand(), EmbedCommand(), TicketCommand(),
                CloseCommand(), WhatismyidCommand(), LinkCommand(), ProfileCommand(), IpCommand()
            )
            .setActivity(Activity.playing("play.IngeniaMC.net on 1.19.x"))
            .build()
    }

    fun shutdown() = jda.shutdown()

}