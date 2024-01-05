package me.m64diamondstar.ingeniamccore.protect.listeners

import org.bukkit.entity.Camel
import org.bukkit.entity.Horse
import org.bukkit.entity.Player
import org.bukkit.entity.Sniffer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.vehicle.VehicleEnterEvent

class PlayerListeners: Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerItemPickup(event: EntityPickupItemEvent){
        if(event.entity !is Player) return
        if(event.entity.hasPermission("ingenia.admin") || event.entity.isOp) return
        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerEnterVehicle(event: VehicleEnterEvent){
        if(event.entered !is Player) return
        if(event.entered.hasPermission("ingenia.admin") || event.entered.isOp) return
        if(event.vehicle is Camel || event.vehicle is Horse || event.vehicle is Sniffer) event.isCancelled = true
    }

}