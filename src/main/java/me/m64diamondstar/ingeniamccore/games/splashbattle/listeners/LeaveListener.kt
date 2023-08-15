package me.m64diamondstar.ingeniamccore.games.splashbattle.listeners

import me.m64diamondstar.ingeniamccore.games.splashbattle.SplashBattleUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class LeaveListener: Listener {

    @EventHandler
    fun onPlayerLeave(e: PlayerQuitEvent) {
        val player = e.player
        if (SplashBattleUtils.players.contains(player)) SplashBattleUtils.leave(player)
    }

}