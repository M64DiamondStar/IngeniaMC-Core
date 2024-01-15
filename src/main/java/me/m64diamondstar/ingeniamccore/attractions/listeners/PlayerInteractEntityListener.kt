package me.m64diamondstar.ingeniamccore.attractions.listeners

import me.m64diamondstar.ingeniamccore.attractions.custom.Coaster
import me.m64diamondstar.ingeniamccore.attractions.custom.FreeFall
import me.m64diamondstar.ingeniamccore.attractions.custom.Frisbee
import me.m64diamondstar.ingeniamccore.attractions.custom.Slide
import me.m64diamondstar.ingeniamccore.utils.entities.EntityUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent

class PlayerInteractEntityListener: Listener {

    @EventHandler
    fun onEntityClick(event: PlayerInteractEntityEvent){

        val entity = event.rightClicked
        if(EntityUtils.isLocked(entity)) return

        Coaster.clickEvent(event.rightClicked)
        Slide.clickEvent(event.rightClicked)
    }

    @EventHandler
    fun onAtEntityClick(event: PlayerInteractAtEntityEvent){

        val entity = event.rightClicked
        if(EntityUtils.isLocked(entity)) return

        FreeFall.clickEvent(event)
        Frisbee.clickEvent(event)
    }
}