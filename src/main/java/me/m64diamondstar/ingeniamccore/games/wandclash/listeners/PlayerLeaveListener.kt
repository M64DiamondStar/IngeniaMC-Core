package me.m64diamondstar.ingeniamccore.games.wandclash.listeners

import me.m64diamondstar.ingeniamccore.games.wandclash.WandClashArenaManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerLeaveListener: Listener {

    @EventHandler
    fun onPlayerLeaveEvent(event: PlayerQuitEvent){
        WandClashArenaManager.leave(event.player)
    }

}