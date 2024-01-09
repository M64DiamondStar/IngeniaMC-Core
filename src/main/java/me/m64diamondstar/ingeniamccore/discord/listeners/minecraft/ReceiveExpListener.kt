package me.m64diamondstar.ingeniamccore.discord.listeners.minecraft

import me.m64diamondstar.ingeniamccore.discord.bot.DiscordBot
import me.m64diamondstar.ingeniamccore.discord.commands.BotUtils
import me.m64diamondstar.ingeniamccore.utils.event.player.ReceiveExpEvent
import net.dv8tion.jda.api.EmbedBuilder
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.awt.Color
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReceiveExpListener: Listener {
    @EventHandler
    fun onReceive(event: ReceiveExpEvent){
        if(BotUtils.LogsUtils.minecraftLogChannel == null) return
        val embedBuilder = EmbedBuilder()
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        val timeNow = LocalDateTime.now()

        embedBuilder.setAuthor("${event.player.name} (${event.player.uniqueId})", null, "https://visage.surgeplay.com/face/${event.player.uniqueId}.png")
        embedBuilder.setDescription("${if(event.amount < 0) "" else "+"}${event.amount} EXP.")
        embedBuilder.setFooter(
            "Online: ${Bukkit.getServer().onlinePlayers.size}/${Bukkit.getServer().maxPlayers}" +
                    "  ${dateTimeFormatter.format(timeNow)}", null
        )
        embedBuilder.setColor(Color.decode("#65d67d"))
        DiscordBot.jda.getTextChannelById(BotUtils.LogsUtils.minecraftLogChannel!!.id)?.sendMessageEmbeds(embedBuilder.build())?.queue()
    }
}