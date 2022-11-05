package me.m64diamondstar.ingeniamccore.general.listeners

import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinListener : Listener {

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val bukkitPlayer = e.player
        val player = IngeniaPlayer(bukkitPlayer)
        player.startUp()
        player.setScoreboard(true)
        player.setTablist(true)
        e.joinMessage = player.joinMessage

        AttractionUtils.getAllAttractions().forEach { it.spawnRidecountSign(bukkitPlayer) }

        player.giveMenuItem()
    }

}