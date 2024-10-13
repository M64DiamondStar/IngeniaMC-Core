package me.m64diamondstar.ingeniamccore.discord.commands.utils

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color

class IpCommand: ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if(!event.name.equals("ip", ignoreCase = true))
            return

        event.deferReply(true).queue()

        val embedBuilder = EmbedBuilder()

        embedBuilder.setDescription("IP: `play.IngeniaMC.net`\n" +
                "Version: `1.21 - 1.21.1`")
        embedBuilder.setColor(Color.decode("#ffb833"))

        event.hook.sendMessageEmbeds(embedBuilder.build()).queue()

    }

}