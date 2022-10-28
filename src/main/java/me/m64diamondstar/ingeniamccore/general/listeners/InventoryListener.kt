package me.m64diamondstar.ingeniamccore.general.listeners

import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerSwapHandItemsEvent

class InventoryListener: Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent){
        val player = event.whoClicked

        if(event.clickedInventory?.type != InventoryType.PLAYER) return
        if(player.hasPermission("ingenia.inventory.move") || player.isOp) return
        if(player.gameMode != GameMode.ADVENTURE && player.gameMode != GameMode.SURVIVAL) return

        event.isCancelled = true
    }

    @EventHandler
    fun onItemSwap(event: PlayerSwapHandItemsEvent) {
        val player = event.player

        if(player.hasPermission("ingenia.inventory.move") || player.isOp) return
        if(player.gameMode != GameMode.ADVENTURE && player.gameMode != GameMode.SURVIVAL) return

        event.isCancelled = true
    }

}