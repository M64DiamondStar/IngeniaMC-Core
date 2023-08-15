package me.m64diamondstar.ingeniamccore.games.splashbattle.listeners

import me.m64diamondstar.ingeniamccore.games.splashbattle.SplashBattleUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent

class TeleportListener: Listener {

    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent){
        if (SplashBattleUtils.players.contains(event.player) && !SplashBattleUtils.dead.contains(event.player))
            event.isCancelled = true

        SplashBattleUtils.dead.remove(event.player)
    }

}