package me.m64diamondstar.ingeniamccore.discord.commands.utils

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color

class WhatismyidCommand: ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if(!event.name.equals("whatismyid", ignoreCase = true))
            return

        event.deferReply(true).queue()

        val embedBuilder = EmbedBuilder()

        embedBuilder.setTitle("What is my ID?")
        embedBuilder.setDescription("Your ID is:\n" +
                "```${event.user.id}```")
        embedBuilder.setColor(Color.decode("#ffb833"))

        event.hook.sendMessageEmbeds(embedBuilder.build()).queue()

    }

}