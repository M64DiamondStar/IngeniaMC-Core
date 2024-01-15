package me.m64diamondstar.ingeniamccore.protect.listeners

import org.bukkit.entity.Camel
import org.bukkit.entity.Entity
import org.bukkit.entity.Horse
import org.bukkit.entity.Llama
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryOpenEvent

class InventoryListener: Listener {

    @EventHandler
    fun onInventoryOpen(event: InventoryOpenEvent){
        if(event.player.hasPermission("ingenia.admin") || event.player.isOp) return
        if(event.inventory.holder !is Entity) return

        val entity = event.inventory.holder

        if(entity is Horse || entity is Camel || entity is Llama) event.isCancelled = true
    }

}