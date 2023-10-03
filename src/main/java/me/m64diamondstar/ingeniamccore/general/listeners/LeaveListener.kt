package me.m64diamondstar.ingeniamccore.general.listeners

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.discord.webhook.DiscordWebhook
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.TeamHandler
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import java.awt.Color
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

class LeaveListener : Listener {

    @EventHandler
    fun onPlayerLeave(e: PlayerQuitEvent) {
        val bukkitPlayer = e.player
        val player = IngeniaPlayer(bukkitPlayer)
        e.quitMessage = player.leaveMessage

        if(TeamHandler.containsPlayer(bukkitPlayer))
            TeamHandler.removePlayer(bukkitPlayer)

        if(IngeniaMC.plugin.config.getBoolean("Discord.Webhook.Enable"))
            thread {
                // Send Discord Webhook
                val discordWebhook = DiscordWebhook(IngeniaMC.plugin.config.getString("Discord.Webhook.Chat"))

                val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                val timeNow = LocalDateTime.now()

                discordWebhook.addEmbed(
                    DiscordWebhook.EmbedObject()
                        .setAuthor(player.name, null, "https://visage.surgeplay.com/face/${player.player.uniqueId}.png")
                        .setDescription("*Left the server*")
                        .setFooter(
                            "Online: ${Bukkit.getServer().onlinePlayers.size - 1}/${Bukkit.getServer().maxPlayers}" +
                                    "  ${dateTimeFormatter.format(timeNow)}", null
                        )
                        .setColor(Color.decode("#D14B4B"))
                )

                discordWebhook.execute()
            }
    }

}