package me.m64diamondstar.ingeniamccore.discord.listeners

import me.m64diamondstar.ingeniamccore.discord.commands.BotUtils
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LogsListener: ListenerAdapter() {

    /*
     * Join Listener
     */
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        val user = event.user
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        val timeNow = LocalDateTime.now()

        val embedBuilder = EmbedBuilder()
        embedBuilder.setTitle("User Joined")
        embedBuilder.setDescription(
            "${user.asMention} joined the server.\n" +
            "Total amount of users: \n" +
            "${event.guild.memberCount}"
        )
        embedBuilder.setFooter(dateTimeFormatter.format(timeNow))
        embedBuilder.setColor(Color.decode(MessageType.SUCCESS))

        BotUtils.LogsUtils.logChannel?.sendMessageEmbeds(embedBuilder.build())?.queue()
    }

    /*
     * Leave Listener
     */
    override fun onGuildMemberRemove(event: GuildMemberRemoveEvent) {
        val user = event.user
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        val timeNow = LocalDateTime.now()

        val embedBuilder = EmbedBuilder()
        embedBuilder.setTitle("User Left")
        embedBuilder.setDescription(
            "${user.asTag} left the server.\n" +
                    "Total amount of users: \n" +
                    "${event.guild.memberCount}"
        )
        embedBuilder.setFooter(dateTimeFormatter.format(timeNow))
        embedBuilder.setColor(Color.decode(MessageType.ERROR))

        BotUtils.LogsUtils.logChannel?.sendMessageEmbeds(embedBuilder.build())?.queue()
    }

    /*
     * Attachment & link Listener
     */
    override fun onMessageReceived(event: MessageReceivedEvent) {
        val user = event.author
        val message = event.message
        val attachments = message.attachments
        val links = extractLinks(message.contentRaw)


        if(attachments.isNotEmpty() || links.isNotEmpty()) {
            val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            val timeNow = LocalDateTime.now()

            val stringBuilder = StringBuilder()
            for(attachment in attachments)
                stringBuilder.append("- " + attachment.url + "\n")

            for(link in links)
                stringBuilder.append("- $link\n")

            val embedBuilder = EmbedBuilder()
            embedBuilder.setTitle("Message Attachment")
            embedBuilder.setDescription(
                "${user.asMention} sent a message with attachments:\n" +
                        "$stringBuilder\n" +
                        "\n" +
                        "Message: ${message.jumpUrl}"
            )
            embedBuilder.setFooter(dateTimeFormatter.format(timeNow))
            embedBuilder.setColor(Color.decode(MessageType.WARNING))

            BotUtils.LogsUtils.logChannel?.sendMessageEmbeds(embedBuilder.build())?.queue()
        }
    }

    override fun onMessageUpdate(event: MessageUpdateEvent) {
        val user = event.author
        val message = event.message
        val attachments = message.attachments
        val links = extractLinks(message.contentRaw)


        if(attachments.isNotEmpty() || links.isNotEmpty()) {
            val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            val timeNow = LocalDateTime.now()

            val stringBuilder = StringBuilder()
            for(attachment in attachments)
                stringBuilder.append("- " + attachment.url + "\n")

            for(link in links)
                stringBuilder.append("- $link\n")

            val embedBuilder = EmbedBuilder()
            embedBuilder.setTitle("Message Attachment")
            embedBuilder.setDescription(
                "${user.asMention} sent a message with attachments:\n" +
                        "$stringBuilder\n" +
                        "\n" +
                        "Message: ${message.jumpUrl}"
            )
            embedBuilder.setFooter(dateTimeFormatter.format(timeNow))
            embedBuilder.setColor(Color.decode(MessageType.WARNING))

            BotUtils.LogsUtils.logChannel?.sendMessageEmbeds(embedBuilder.build())?.queue()
        }
    }



    private fun extractLinks(text: String): List<String> {
        val urlPattern = Regex("""https?://\S+|www\.\S+""")
        val matches = urlPattern.findAll(text)
        return matches.map { it.value }.toList()
    }

}