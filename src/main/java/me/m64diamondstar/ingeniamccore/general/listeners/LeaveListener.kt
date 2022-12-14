package me.m64diamondstar.ingeniamccore.general.listeners

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.TeamHandler
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class LeaveListener : Listener {
    @EventHandler
    fun onPlayerLeave(e: PlayerQuitEvent) {
        val bukkitPlayer = e.player
        val player = IngeniaPlayer(bukkitPlayer)
        e.quitMessage = player.leaveMessage

        if(TeamHandler.containsPlayer(bukkitPlayer))
            TeamHandler.removePlayer(bukkitPlayer)
    }
}