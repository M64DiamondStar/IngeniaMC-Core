package me.m64diamondstar.ingeniamccore.general.listeners

import me.m64diamondstar.ingeniamccore.cosmetics.data.JoinLeaveMessage
import me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageBuilder
import me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType
import me.m64diamondstar.ingeniamccore.discord.bot.DiscordBot
import me.m64diamondstar.ingeniamccore.discord.commands.BotUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import java.util.*

class JoinListener : Listener {

    companion object {
        // Used to send packets when player moves (only after joining)
        // This is necessary to send certain packets
        val logged = ArrayList<UUID>()
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val bukkitPlayer = e.player
        val player = IngeniaPlayer(bukkitPlayer)
        e.joinMessage = MessageBuilder.JoinMessageBuilder(player.name, player.joinColor, player.joinMessage).build()

        player.startUp()

        if(BotUtils.ChatUtils.chatChannel != null){
            val joinLeaveMessage = JoinLeaveMessage(MessageType.JOIN)
            DiscordBot.jda.getTextChannelById(BotUtils.ChatUtils.chatChannel!!.id)?.sendMessage(
                "${BotUtils.EmojiUtils.getJoinEmoji()} " +
                        "${joinLeaveMessage.getMessage(player.joinMessage ?: "default")?.replace("%player%", player.name)?.replace("_", "\\_")}"
            )?.queue()
        }
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent){
        if(logged.contains(event.player.uniqueId)) return

        // Enable the tab list for the joined player
        val ingeniaPlayer = IngeniaPlayer(event.player)
        ingeniaPlayer.setTablist()

        logged.add(event.player.uniqueId)
    }

}