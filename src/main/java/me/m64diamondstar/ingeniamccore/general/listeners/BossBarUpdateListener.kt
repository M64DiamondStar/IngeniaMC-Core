package me.m64diamondstar.ingeniamccore.general.listeners

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.event.player.ReceiveGoldenStarsEvent
import me.m64diamondstar.ingeniamccore.utils.event.player.SwitchAreaEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class BossBarUpdateListener: Listener {

    @EventHandler
    fun onReceiveGoldenStars(event: ReceiveGoldenStarsEvent){
        val player = IngeniaPlayer(event.player)
        player.updateMainBossBar()
    }

    @EventHandler
    fun onSwitchArea(event: SwitchAreaEvent){
        val player = IngeniaPlayer(event.player)
        player.updateMainBossBar()
    }

}