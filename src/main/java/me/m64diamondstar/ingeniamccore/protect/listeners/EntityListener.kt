package me.m64diamondstar.ingeniamccore.protect.listeners

import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerLeashEntityEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
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

    @EventHandler
    fun onAtEntityInteract(event: PlayerInteractAtEntityEvent) {
        val player = event.player
        val entity = event.rightClicked

        if(player.hasPermission("ingenia.admin") || player.isOp) return
        if(!(entity.type == EntityType.ARMOR_STAND || entity.type == EntityType.ITEM_FRAME || entity.type == EntityType.GLOW_ITEM_FRAME)) return

        event.isCancelled = true
    }

    @EventHandler
    fun onEntityInteract(event: PlayerInteractEntityEvent){
        val player = event.player
        if(event.isCancelled) return
        if(event.rightClicked !is ItemFrame) return
        if(player.hasPermission("ingenia.admin")) return
        event.isCancelled = true
    }

}