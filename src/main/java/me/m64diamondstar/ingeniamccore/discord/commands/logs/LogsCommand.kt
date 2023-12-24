package me.m64diamondstar.ingeniamccore.discord.commands.logs

import me.m64diamondstar.ingeniamccore.discord.commands.BotUtils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.buttons.Button
import java.awt.Color

class LogsCommand: ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != "ticket") return
        if (BotUtils.findRole(event.member!!, "Lead") == null) { // User doesn't have Lead
            event.deferReply(true).queue()
            if (BotUtils.TicketUtils.ticketChannel == null)
                event.hook.sendMessage(
                    "The ticket system is not functioning right now. " +
                            "Please contact a Lead to fix this problem."
                ).queue()
            else
                event.hook.sendMessage("Please create a ticket in ${BotUtils.TicketUtils.ticketChannel!!.asMention}.")
                    .queue()
        } else { // User has Lead
            event.deferReply(true).queue()

            val embedBuilder = EmbedBuilder()

            embedBuilder.setTitle("Ticket Configuration")
            embedBuilder.setDescription("Please select the subject you\nwould like to configure.")
            embedBuilder.setThumbnail("https://ingeniamc.net/images/nametag.png")
            embedBuilder.setColor(Color.decode("#ffb833"))

            val setTicketChannel = Button.primary("setTicketChannel", "Set Ticket Channel")
            val setLogChannel = Button.primary("setLogChannel", "Set Log Channel")

            event.hook.sendMessageEmbeds(embedBuilder.build()).addActionRow(setTicketChannel, setLogChannel).queue()
        }
    }
}