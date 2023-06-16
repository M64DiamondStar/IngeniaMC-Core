package me.m64diamondstar.ingeniamccore.general.listeners

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.discord.webhook.DiscordWebhook
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.awt.Color
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

class JoinListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val bukkitPlayer = e.player
        val player = IngeniaPlayer(bukkitPlayer)
        e.joinMessage = player.joinMessage

        player.startUp()

        thread {
            // Send Discord Webhook
            val discordWebhook = DiscordWebhook(IngeniaMC.plugin.config.getString("Discord.Webhook.Chat"))

            val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            val timeNow = LocalDateTime.now()

            discordWebhook.addEmbed(
                DiscordWebhook.EmbedObject()
                    .setAuthor(player.name, null, "https://visage.surgeplay.com/face/${player.player.uniqueId}.png")
                    .setDescription("*Joined the server*")
                    .setFooter(
                        "Online: ${Bukkit.getServer().onlinePlayers.size}/${Bukkit.getServer().maxPlayers}" +
                                "  ${dateTimeFormatter.format(timeNow)}", null
                    )
                    .setColor(Color.decode("#76D173"))
            )

            discordWebhook.execute()
        }
    }

}