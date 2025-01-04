package me.m64diamondstar.ingeniamccore.discord.bot

import me.m64diamondstar.ingeniamccore.discord.commands.BotUtils
import net.dv8tion.jda.api.EmbedBuilder
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.awt.Color
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface DiscordLogType {
    companion object {
        const val EXTREME = "#bd4d4d"
        const val SEVERE = "#e08282"
        const val BACKGROUND = "#858585"
        const val SUCCESS = "#53bd4d"
        const val INFO = "#6ac4c1"
        const val WARNING = "#cc9349"
        const val INGENIA = "#f4b734"
        const val DEFAULT = "#bfbfbf"
    }
}

fun logToDiscord(player: Player, message: String, color: String = DiscordLogType.DEFAULT) {
    if(BotUtils.LogsUtils.minecraftLogChannel == null) return
    val embedBuilder = EmbedBuilder()
    val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    val timeNow = LocalDateTime.now()

    embedBuilder.setAuthor("${player.name} (${player.uniqueId})", null, "https://visage.surgeplay.com/face/${player.uniqueId}.png")
    embedBuilder.setDescription(message)
    embedBuilder.setFooter(
        "Online: ${Bukkit.getServer().onlinePlayers.size}/${Bukkit.getServer().maxPlayers}" +
                "  ${dateTimeFormatter.format(timeNow)}", null
    )
    embedBuilder.setColor(Color.decode(color))
    DiscordBot.jda.getTextChannelById(BotUtils.LogsUtils.minecraftLogChannel!!.id)?.sendMessageEmbeds(embedBuilder.build())?.queue()
}

fun logToDiscord(title: String, message: String, color: String = DiscordLogType.DEFAULT) {
    if(BotUtils.LogsUtils.minecraftLogChannel == null) return
    val embedBuilder = EmbedBuilder()
    val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    val timeNow = LocalDateTime.now()

    embedBuilder.setTitle(title)
    embedBuilder.setDescription(message)
    embedBuilder.setFooter(
        "Online: ${Bukkit.getServer().onlinePlayers.size}/${Bukkit.getServer().maxPlayers}" +
                "  ${dateTimeFormatter.format(timeNow)}", null
    )
    embedBuilder.setColor(Color.decode(color))
    DiscordBot.jda.getTextChannelById(BotUtils.LogsUtils.minecraftLogChannel!!.id)?.sendMessageEmbeds(embedBuilder.build())?.queue()
}