package me.m64diamondstar.ingeniamccore.discord.commands.logs

import me.m64diamondstar.ingeniamccore.discord.commands.BotUtils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.buttons.Button
import java.awt.Color

class LogsCommand: ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != "logs") return
        if (BotUtils.findRole(event.member!!, "Lead") != null){ // User has Lead
            event.deferReply(true).queue()

            val embedBuilder = EmbedBuilder()

            embedBuilder.setTitle("Ticket Configuration")
            embedBuilder.setDescription("Please select the subject you\nwould like to configure.")
            embedBuilder.setThumbnail("https://ingeniamc.net/images/nametag.png")
            embedBuilder.setColor(Color.decode("#ffb833"))

            val setLogChannel = Button.primary("logs-setLogChannel", "Set Log Channel")

            event.hook.sendMessageEmbeds(embedBuilder.build()).addActionRow(setLogChannel).queue()
        }
    }


    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if(event.button.id != "logs-setLogChannel") return
        event.deferReply(true).queue()
        BotUtils.LogsUtils.logChannel = event.channel as TextChannel
        event.hook.sendMessage("Successfully updated the log channel").queue()
    }
}