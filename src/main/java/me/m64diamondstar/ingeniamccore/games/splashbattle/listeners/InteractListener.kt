package me.m64diamondstar.ingeniamccore.games.splashbattle.listeners

import me.m64diamondstar.ingeniamccore.games.splashbattle.SplashBattleUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class InteractListener: Listener {

    @EventHandler
    fun onPlayerClickEvent(e: PlayerInteractEvent) {
        val player = e.player
        if (e.action != Action.RIGHT_CLICK_BLOCK) return
        if (e.clickedBlock == null) return
        if (!e.clickedBlock!!.type.toString().contains("SIGN")) return

        for(splashBattle in SplashBattleUtils.getAllSplashBattles()){
            if(splashBattle.joinSignLocation.x == e.clickedBlock!!.location.x &&
                splashBattle.joinSignLocation.y == e.clickedBlock!!.location.y &&
                splashBattle.joinSignLocation.z == e.clickedBlock!!.location.z)

                SplashBattleUtils.join(player, splashBattle)
        }
    }

}