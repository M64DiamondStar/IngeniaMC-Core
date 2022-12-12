package me.m64diamondstar.ingeniamccore.attractions.listeners

import me.m64diamondstar.ingeniamccore.attractions.custom.Coaster
import me.m64diamondstar.ingeniamccore.attractions.custom.FreeFall
import me.m64diamondstar.ingeniamccore.attractions.custom.Slide
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent

class PlayerInteractEntityListener: Listener {

    @EventHandler
    fun onEntityClick(event: PlayerInteractEntityEvent){
        Coaster.clickEvent(event.rightClicked)
        Slide.clickEvent(event.rightClicked)
    }

    @EventHandler
    fun onAtEntityClick(event: PlayerInteractAtEntityEvent){
        FreeFall.clickEvent(event)
    }
}