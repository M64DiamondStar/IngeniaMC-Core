package me.m64diamondstar.ingeniamccore.general.listeners

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerCommandSendEvent

class ChatListener: Listener {

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent){
        val player = event.player
        val ingeniaPlayer = IngeniaPlayer(player)
        event.format = Colors.format("${ingeniaPlayer.prefix}&r ${ingeniaPlayer.name} » ${event.message.replace("%", "%%")}")
    }

    @EventHandler
    fun onPlayerCommandSend(event: PlayerCommandSendEvent){
        val player = event.player

        if(player.hasPermission("ingenia.team")) return

        event.commands.clear()
        event.commands.addAll( IngeniaMC.plugin.config.getStringList("Player-Commands"))
    }

}