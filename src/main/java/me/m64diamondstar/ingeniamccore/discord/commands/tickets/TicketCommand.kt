package me.m64diamondstar.ingeniamccore.discord.commands.tickets

import me.m64diamondstar.ingeniamccore.discord.commands.BotUtils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.buttons.Button
import java.awt.Color
import java.util.*
import kotlin.concurrent.thread

class TicketCommand: ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != "ticket") return
        if (BotUtils.findRole(event.member!!, "Lead") == null) { // User doesn't have Lead
            event.deferReply(true).queue()
            if(BotUtils.TicketUtils.ticketChannel == null)
                event.hook.sendMessage("The ticket system is not functioning right now. " +
                    "Please contact a Lead to fix this problem.").queue()
            else
                event.hook.sendMessage("Please create a ticket in ${BotUtils.TicketUtils.ticketChannel!!.asMention}.").queue()
        }

        else { // User has Lead
            event.deferReply(true).queue()

            val embedBuilder = EmbedBuilder()

            embedBuilder.setTitle("Ticket Configuration")
            embedBuilder.setDescription("Please select the subject you\nwould like to configure.")
            embedBuilder.setThumbnail("https://ingeniamc.net/images/nametag.png")
            embedBuilder.setColor(Color.decode("#ffb833"))

            val setTicketChannel = Button.primary("setTicketChannel", "Set Ticket Channel")
            val setLogChannel = Button.primary("setTicketLogChannel", "Set Log Channel")

            event.hook.sendMessageEmbeds(embedBuilder.build()).addActionRow(setTicketChannel, setLogChannel).queue()
        }

    }

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if(event.member == null) return

        when (event.button.id) {

            "setTicketChannel" -> {
                if(BotUtils.TicketUtils.ticketMessageID != null && BotUtils.TicketUtils.ticketChannel != null){
                    // Remove old message
                    BotUtils.TicketUtils.ticketChannel?.deleteMessageById(BotUtils.TicketUtils.ticketMessageID!!)?.queue()
                }

                val embedBuilder = EmbedBuilder()

                embedBuilder.setTitle("Tickets")
                embedBuilder.setDescription("Please click the button below to create \na new ticket. Further " +
                        "details will\n" +
                        "be given inside of the ticket.\n" +
                        "\n" +
                        "*What is a ticket used for?*\n" +
                        "- When you want to **apply for team**\n" +
                        "- When you get an **error on the store**\n" +
                        "- To **report players or bugs**\n" +
                        "- **Questions** that can't be asked publicly")
                embedBuilder.setThumbnail("https://ingeniamc.net/images/nametag.png")
                embedBuilder.setColor(Color.decode("#ffb833"))

                val setTicketChannel = Button.primary("openTicket", "Create")

                // Send message & update data in plugin config
                event.channel.asTextChannel().sendMessageEmbeds(embedBuilder.build()).addActionRow(setTicketChannel).queue {
                    BotUtils.TicketUtils.ticketChannel = event.channel as TextChannel
                    BotUtils.TicketUtils.ticketMessageID = it.idLong
                }

                // Makes sure to cancel the interaction failed message
                event.deferEdit().queue()
            }

            "setTicketLogChannel" -> {
                event.deferReply(true).queue()

                BotUtils.TicketUtils.logChannel = event.channel as TextChannel

                event.hook.sendMessage("Successfully updated the log channel").queue()
            }

            "openTicket" -> {
                thread { // Run this in a new thread because of usage of Action.complete()
                    event.deferReply(true).queue()

                    if (event.guild?.getCategoriesByName("tickets", true) == null ||
                        event.guild?.getCategoriesByName("tickets", true)!!.size == 0
                    ) { // Create category if it doesn't exist
                        event.guild?.createCategory("tickets")?.complete()
                    }

                    event.guild?.getCategoriesByName(
                        "tickets",
                        true
                    )!![0]?.textChannels?.forEach {
                        if(event.user.idLong == it.topic?.replace("ID: ", "")?.toLong()){
                            event.hook.sendMessage("You can't create more than one ticket at a time.\n" +
                                    "Please close ${it.asMention} before creating a new ticket.").queue()
                            return@thread
                        }
                    }

                    BotUtils.TicketUtils.ticketCount += 1

                    event.guild?.getCategoriesByName(
                        "tickets",
                        true
                    )!![0]?.createTextChannel("ticket-${BotUtils.TicketUtils.ticketCount}")
                        ?.addPermissionOverride(event.member!!, getTicketHolderPreAllowedPermissions(), getTicketHolderPreDisallowedPermissions())
                        ?.queue {
                        event.hook.sendMessage("Successfully created ${it.asMention}").queue()
                        it.manager.setTopic("ID: ${event.user.id}").queue()

                        // Send Ticket Settings
                        val embedBuilder = EmbedBuilder()

                        embedBuilder.setTitle("${event.user.name}'s Ticket")
                        embedBuilder.setDescription("Please select one of the options below.\n" +
                                "If you created this ticket for another\n" +
                                "reason then please select 'Other'.")
                        embedBuilder.setThumbnail("https://ingeniamc.net/images/book_and_quil.png")
                        embedBuilder.setColor(Color.decode("#ffb833"))

                        val applyButton = Button.primary("ticketTeamApply", "Team Apply")
                        val purchaseButton = Button.primary("ticketPurchaseError", "Purchase Error")
                        val playerButton = Button.primary("ticketPlayerReport", "Player Report")
                        val bugButton = Button.primary("ticketBugReport", "Bug Report")
                        val otherButton = Button.secondary("ticketOther", "Other")
                        val closeButton = Button.danger("closeTicket", "Close") // This one gets handled in CloseCommand.kt

                        it.sendMessageEmbeds(embedBuilder.build()).addActionRow(applyButton, purchaseButton, playerButton)
                            .addActionRow(bugButton, otherButton, closeButton).queue()

                        it.manager.putPermissionOverride(event.guild!!.getRolesByName("@everyone", true)[0],
                                0L, 1024L).queue()
                    }

                }
            }

            // Ticket Types
            "ticketTeamApply" -> {
                event.deferEdit().queue()
                event.channel.asTextChannel().manager.putPermissionOverride(event.member!!, getTicketHolderAllowedPermissions(), getTicketHolderDisallowedPermissions()).queue()
                event.message.delete().queue()

                val embedBuilder = EmbedBuilder()

                embedBuilder.setTitle("${event.user.name}'s Team Apply")
                embedBuilder.setDescription("Please answer the questions below.\n\n" +
                        "1) What is your Minecraft Username?\n" +
                        "2) What should we call you?\n" +
                        "3) How old are you?\n" +
                        "4) How do you want to help us? (building, modelling, coding, ...)\n" +
                        "5) Do you already have experience? Proof will also help!\n" +
                        "6) What is your motivation to work with us?\n" +
                        "\n" +
                        "Thank you very much for applying, we'll get in contact as soon as possible!" +
                        "\n" +
                        "By applying you agree to our [Terms and Conditions](https://ingeniamc.net/team-tac).")
                embedBuilder.setThumbnail("https://ingeniamc.net/images/bricks.png")
                embedBuilder.setColor(Color.decode("#ffb833"))

                val closeButton = Button.danger("closeTicket", "Close") // This one gets handled in CloseCommand.kt

                event.channel.sendMessageEmbeds(embedBuilder.build()).addActionRow(closeButton).queue()
                event.channel.asTextChannel().manager.setName(event.channel.name + "-a").queue()
            }

            "ticketPurchaseError" -> {
                event.deferEdit().queue()
                event.channel.asTextChannel().manager.putPermissionOverride(event.member!!, getTicketHolderAllowedPermissions(), getTicketHolderDisallowedPermissions()).queue()
                event.message.delete().queue()

                val embedBuilder = EmbedBuilder()

                embedBuilder.setTitle("${event.user.name}'s Purchase Error")
                embedBuilder.setDescription("Please answer the questions below.\n\n" +
                        "1) What is your Minecraft Username?\n" +
                        "2) What is your order ID?\n" +
                        "3) What went wrong? (Ex. package not received)\n" +
                        "\n" +
                        "Thank you very much, we'll get in contact as soon as possible!")
                embedBuilder.setThumbnail("https://ingeniamc.net/images/ender_chest.png")
                embedBuilder.setColor(Color.decode("#ffb833"))

                val closeButton = Button.danger("closeTicket", "Close") // This one gets handled in CloseCommand.kt

                event.channel.sendMessageEmbeds(embedBuilder.build()).addActionRow(closeButton).queue()
                event.channel.asTextChannel().manager.setName(event.channel.name + "-e").queue()
            }

            "ticketPlayerReport" -> {
                event.deferEdit().queue()
                event.channel.asTextChannel().manager.putPermissionOverride(event.member!!, getTicketHolderAllowedPermissions(), getTicketHolderDisallowedPermissions()).queue()
                event.message.delete().queue()

                val embedBuilder = EmbedBuilder()

                embedBuilder.setTitle("${event.user.name}'s Player Report")
                embedBuilder.setDescription("Please answer the questions below.\n\n" +
                        "**For Discord:**\n" +
                        "1) What is the full username? (ex. abc#1234)\n" +
                        "2) Which rule did this person violate?\n" +
                        "3) Do you have proof? (Picture or video)\n" +
                        "\n" +
                        "**For Minecraft:**\n" +
                        "1) What is the Minecraft Username of the rule breaker?\n" +
                        "2) Which rule did this person break?\n" +
                        "3) Do you have proof? (Picture or video)\n" +
                        "\n" +
                        "**Please ping us if it is urgent!**\n" +
                        "Thank you very much, we'll get in contact as soon as possible!")
                embedBuilder.setThumbnail("https://ingeniamc.net/images/anvil.png")
                embedBuilder.setColor(Color.decode("#ffb833"))

                val closeButton = Button.danger("closeTicket", "Close") // This one gets handled in CloseCommand.kt

                event.channel.sendMessageEmbeds(embedBuilder.build()).addActionRow(closeButton).queue()
                event.channel.asTextChannel().manager.setName(event.channel.name + "-p").queue()
            }

            "ticketBugReport" -> {
                event.deferEdit().queue()
                event.channel.asTextChannel().manager.putPermissionOverride(event.member!!, getTicketHolderAllowedPermissions(), getTicketHolderDisallowedPermissions()).queue()
                event.message.delete().queue()

                val embedBuilder = EmbedBuilder()

                embedBuilder.setTitle("${event.user.name}'s Bug Report")
                embedBuilder.setDescription("Please answer the questions below.\n\n" +
                        "1) Is it a bug in the Minecraft or Discord server?\n" +
                        "2) Please explain the bug.\n" +
                        "3) Do you have a picture or video demonstrating it?\n" +
                        "\n" +
                        "**Please ping us if it is urgent!**\n" +
                        "Thank you very much, we'll get in contact as soon as possible!")
                embedBuilder.setThumbnail("https://ingeniamc.net/images/command_block.png")
                embedBuilder.setColor(Color.decode("#ffb833"))

                val closeButton = Button.danger("closeTicket", "Close") // This one gets handled in CloseCommand.kt

                event.channel.sendMessageEmbeds(embedBuilder.build()).addActionRow(closeButton).queue()

                event.channel.asTextChannel().manager.setName(event.channel.name + "-b").queue()
            }

            "ticketOther" -> {
                event.deferEdit().queue()
                event.channel.asTextChannel().manager.putPermissionOverride(event.member!!, getTicketHolderAllowedPermissions(), getTicketHolderDisallowedPermissions()).queue()
                event.message.delete().queue()

                val embedBuilder = EmbedBuilder()

                embedBuilder.setTitle("${event.user.name}'s Question")
                embedBuilder.setDescription("Please explain your question.\n" +
                        "\n" +
                        "Thank you very much for contacting us, \n" +
                        "we'll get in touch as soon as possible!")
                embedBuilder.setThumbnail("https://ingeniamc.net/images/bell.png")
                embedBuilder.setColor(Color.decode("#ffb833"))

                val closeButton = Button.danger("closeTicket", "Close") // This one gets handled in CloseCommand.kt

                event.channel.sendMessageEmbeds(embedBuilder.build()).addActionRow(closeButton).queue()

                event.channel.asTextChannel().manager.setName(event.channel.name + "-o").queue()
            }
        }
    }

    private fun getTicketHolderPreAllowedPermissions(): EnumSet<Permission>? {
        return EnumSet.of(
            Permission.VIEW_CHANNEL
        )
    }

    private fun getTicketHolderPreDisallowedPermissions(): EnumSet<Permission>? {
        return EnumSet.of(
            Permission.MESSAGE_MANAGE,
            Permission.MESSAGE_SEND
        )
    }

    private fun getTicketHolderAllowedPermissions(): EnumSet<Permission>? {
        return EnumSet.of(
            Permission.VIEW_CHANNEL,
            Permission.MESSAGE_SEND,
            Permission.MESSAGE_EMBED_LINKS,
            Permission.MESSAGE_HISTORY,
            Permission.MESSAGE_ATTACH_FILES,
            Permission.MESSAGE_EXT_EMOJI,
            Permission.MESSAGE_ADD_REACTION,
            Permission.MESSAGE_TTS,
            Permission.MESSAGE_EXT_STICKER
        )
    }

    private fun getTicketHolderDisallowedPermissions(): EnumSet<Permission>? {
        return EnumSet.of(
            Permission.MESSAGE_MANAGE
        )
    }
}