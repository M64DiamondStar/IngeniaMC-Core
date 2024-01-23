package me.m64diamondstar.ingeniamccore.discord.commands

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.discord.bot.DiscordBot
import me.m64diamondstar.ingeniamccore.general.player.data.DiscordUserConfig
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import java.awt.Color
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.thread

object BotUtils {

    fun findRole(member: Member, name: String): Role? {
        val roles = member.roles
        return roles.stream()
            .filter { role: Role? -> role!!.name == name } // filter by role name
            .findFirst() // take first result
            .orElse(null) // else return null
    }

    object EmojiUtils {

        fun getJoinEmoji(): String {
            return IngeniaMC.plugin.config.getString("Discord.Bot.Join-Emoji") ?: "+"
        }

        fun getLeaveEmoji(): String {
            return IngeniaMC.plugin.config.getString("Discord.Bot.Leave-Emoji") ?: "-"
        }
    }

    object LinkedUtils {

        fun isLinked(discordID: Long): Boolean{
            if(!File(IngeniaMC.plugin.dataFolder, "data/discord-user/$discordID.yml").exists()) return false
            val discordUserConfig = DiscordUserConfig(discordID)
            return discordUserConfig.hasMinecraft()
        }

        fun getLinked(discordID: Long): UUID?{
            if(!File(IngeniaMC.plugin.dataFolder, "data/discord-user/$discordID.yml").exists()) return null
            val discordUserConfig = DiscordUserConfig(discordID)
            return discordUserConfig.getMinecraft()
        }

    }

    object LinkingUtils {
        private val linking = HashMap<Long, UUID>()

        /**
         * Add a Discord user who is currently linking with a Minecraft account.
         */
        fun addLinking(discordID: Long, playerUUID: UUID){
            linking[discordID] = playerUUID
        }

        /**
         * Check if a Discord user is currently linking with a Minecraft account.
         */
        fun isLinking(discordID: Long): Boolean{
            return linking.containsKey(discordID)
        }

        /**
         * Get the Minecraft account a discord user is linking with.
         * @return The UUID of the Minecraft account
         */
        fun getLinking(discordID: Long): UUID?{
            return linking[discordID]
        }

        /**
         * Remove a linking session.
         */
        fun removeLinking(discordID: Long){
            linking.remove(discordID)
        }
    }

    object TicketUtils {

        var ticketChannel: TextChannel?
            set(value) {
                if(value != null) {
                    IngeniaMC.plugin.config.set("Discord.Bot.Tickets.TicketChannelID", value.idLong)
                    IngeniaMC.plugin.saveConfig()
                }
            }
            get() = DiscordBot.jda.getTextChannelById(IngeniaMC.plugin.config.getLong("Discord.Bot.Tickets.TicketChannelID"))

        var ticketMessageID: Long?
            set(value) {
                if(value != null) {
                    IngeniaMC.plugin.config.set("Discord.Bot.Tickets.TicketMessageID", value)
                    IngeniaMC.plugin.saveConfig()
                }
            }
            get() = IngeniaMC.plugin.config.getLong("Discord.Bot.Tickets.TicketMessageID")

        var logChannel: TextChannel?
            set(value) {
                if(value != null) {
                    IngeniaMC.plugin.config.set("Discord.Bot.Tickets.LogChannelID", value.idLong)
                    IngeniaMC.plugin.saveConfig()
                }
            }
            get() = DiscordBot.jda.getTextChannelById(IngeniaMC.plugin.config.getLong("Discord.Bot.Tickets.LogChannelID"))

        var ticketCount: Long
            set(value) {
                IngeniaMC.plugin.config.set("Discord.Bot.Tickets.TicketCount", value)
                IngeniaMC.plugin.saveConfig()
            }
            get() = IngeniaMC.plugin.config.getLong("Discord.Bot.Tickets.TicketCount")

        fun closeTicket(channel: TextChannel, closeUser: User) {

            thread { // Run this in a new thread because of usage of Action.complete()
                var ticketType = ""

                if (channel.name.split("-").size < 3) {
                    ticketType = "Undefined Ticket"
                } else {
                    when (channel.name.split("-")[2]) {
                        "a" -> ticketType = "Team Apply"
                        "e" -> ticketType = "Purchase Error"
                        "p" -> ticketType = "Player Report"
                        "b" -> ticketType = "Bug Report"
                        "o" -> ticketType = "Question"
                    }
                }

                val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                val timeNow = LocalDateTime.now()

                val embedBuilder = EmbedBuilder()

                embedBuilder.setTitle(
                    channel.name + ": " +
                            channel.guild.retrieveMemberById(
                                channel.topic!!.replace("ID: ", "")
                            ).complete().user.name +
                            "'s $ticketType"
                )

                embedBuilder.setDescription(
                    "\n" +
                            "Marked as ***closed***.\n" +
                            "\n" +
                            "Ticket Owner: ${
                                channel.guild.retrieveMemberById(channel.topic!!.replace("ID: ", "")).complete().asMention
                            }\n" +
                            "Ticket Owner ID: ${channel.topic!!.replace("ID: ", "")}\n" +
                            "\n" +
                            "Closed by: ${closeUser.asMention}"
                )
                embedBuilder.setFooter(dateTimeFormatter.format(timeNow))
                embedBuilder.setColor(Color.decode("#ffb833"))

                logChannel?.sendMessageEmbeds(embedBuilder.build())?.queue()

                channel.delete().queue()
            }
        }

    }

    object LogsUtils {
        var logChannel: TextChannel?
            set(value) {
                if(value != null) {
                    IngeniaMC.plugin.config.set("Discord.Bot.Logs.LogChannelID", value.idLong)
                    IngeniaMC.plugin.saveConfig()
                }
            }
            get() = DiscordBot.jda.getTextChannelById(IngeniaMC.plugin.config.getLong("Discord.Bot.Logs.LogChannelID"))

        var minecraftLogChannel: TextChannel?
            set(value) {
                if(value != null) {
                    IngeniaMC.plugin.config.set("Discord.Bot.Logs.MinecraftLogChannelID", value.idLong)
                    IngeniaMC.plugin.saveConfig()
                }
            }
            get() = DiscordBot.jda.getTextChannelById(IngeniaMC.plugin.config.getLong("Discord.Bot.Logs.MinecraftLogChannelID"))
    }

    object ChatUtils {
        var chatChannel: TextChannel?
            set(value) {
                if(value != null) {
                    IngeniaMC.plugin.config.set("Discord.Bot.Chat.ChatChannelID", value.idLong)
                    IngeniaMC.plugin.saveConfig()
                }
            }
            get() = DiscordBot.jda.getTextChannelById(IngeniaMC.plugin.config.getLong("Discord.Bot.Chat.ChatChannelID"))
    }

}