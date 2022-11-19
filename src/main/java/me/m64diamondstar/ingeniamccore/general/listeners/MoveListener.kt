package me.m64diamondstar.ingeniamccore.general.listeners

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent

class MoveListener: Listener {

    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent){
        val player = event.player
        val ingeniaPlayer = IngeniaPlayer(player)
        ingeniaPlayer.setPreviousLocation(event.from)
    }

}