package me.m64diamondstar.ingeniamccore.discord.commands.profile

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ProfileCommand: ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != "profile") return
        event.deferReply(true).queue()
        event.hook.sendMessage("Please use `/link <Discord ID>` in-game.").queue()
    }

}