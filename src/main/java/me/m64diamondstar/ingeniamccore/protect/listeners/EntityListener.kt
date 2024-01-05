package me.m64diamondstar.ingeniamccore.protect.listeners

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerLeashEntityEvent
import org.bukkit.event.vehicle.VehicleDestroyEvent

class EntityListener: Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onCartDestroy(event: VehicleDestroyEvent){
        val attacker = event.attacker
        if(attacker !is Player) return
        if(attacker.hasPermission("ingenia.admin") || attacker.isOp) return
        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onEntityLeach(event: PlayerLeashEntityEvent){
        val player = event.player
        if(player.hasPermission("ingenia.admin") || player.isOp) return
        event.isCancelled = true
    }

}