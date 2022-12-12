package me.m64diamondstar.ingeniamccore.games.parkour.listeners

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class PlayerMoveListener: Listener {

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent){
        val player = event.player
        val ingeniaPlayer = IngeniaPlayer(player)
        if(ingeniaPlayer.isInGame) return

        for(parkour in ParkourUtils.getAllParkours()){
            if(parkour.isInstalled()) {
                if (player.location.distanceSquared(parkour.startLocation!!) <= parkour.startRadius){
                    //Start Parkour, everything that happens next is controlled from this method
                    ingeniaPlayer.startParkour(parkour)
                }
            }
        }
    }

}