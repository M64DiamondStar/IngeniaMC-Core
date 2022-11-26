package me.m64diamondstar.ingeniamccore.general.listeners.protection

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.FoodLevelChangeEvent

class HungerListener: Listener {

    @EventHandler
    fun onHunger(event: FoodLevelChangeEvent){
        if(event.entity !is Player) return

        event.isCancelled = true
    }


}