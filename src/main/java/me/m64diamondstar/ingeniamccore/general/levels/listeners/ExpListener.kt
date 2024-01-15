package me.m64diamondstar.ingeniamccore.general.levels.listeners

import me.m64diamondstar.ingeniamccore.games.PhysicalGameType
import me.m64diamondstar.ingeniamccore.general.levels.LevelUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.event.player.ReceiveExpEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ExpListener: Listener {

    @EventHandler
    fun onPlayerReceiveExp(event: ReceiveExpEvent){
        val ingeniaPlayer = IngeniaPlayer(event.player)
        if(ingeniaPlayer.isInGame && ingeniaPlayer.game == PhysicalGameType.SPLASH_BATTLE) return

        val currentLevel = ingeniaPlayer.getLevel()
        val necessaryExp = LevelUtils.getExpRequirement(currentLevel + 1) - LevelUtils.getExpRequirement(currentLevel)
        val currentExp = ingeniaPlayer.exp - LevelUtils.getExpRequirement(currentLevel)
        event.player.level = ingeniaPlayer.getLevel()
        event.player.exp = currentExp.toFloat() / necessaryExp
    }

}