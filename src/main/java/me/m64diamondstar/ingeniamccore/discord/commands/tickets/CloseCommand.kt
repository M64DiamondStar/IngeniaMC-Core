package me.m64diamondstar.ingeniamccore.discord.commands.tickets

import me.m64diamondstar.ingeniamccore.discord.commands.BotUtils
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.buttons.Button

class CloseCommand: ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != "close") return

        event.deferReply(true).queue()

        if(event.channelType != ChannelType.TEXT || !event.channel.asTextChannel().name.startsWith("ticket-")
            || event.channel.asTextChannel().topic == null
            || !event.channel.asTextChannel().topic!!.startsWith("ID: ")){
            event.hook.sendMessage("You are not in a ticket channel.").queue()
            return
        }

        val closeButton = Button.danger("closeTicket", "Close")
        event.hook.sendMessage("Please confirm that you want to close this ticket\n" +
                "by clicking the button below.").addActionRow(closeButton).queue()
    }

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if(event.button.id != "closeTicket") return
        if(event.channelType != ChannelType.TEXT) return
        BotUtils.TicketUtils.closeTicket(event.channel.asTextChannel(), event.user)
    }

}