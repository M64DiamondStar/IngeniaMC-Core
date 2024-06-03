package me.m64diamondstar.ingeniamccore.general.listeners

import me.m64diamondstar.ingeniamccore.utils.gui.GuiManager
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent

class InventoryListener: Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent){
        val player = event.whoClicked

        if(player.hasPermission("ingenia.inventory.move") || player.isOp) return
        if(player.gameMode != GameMode.ADVENTURE && player.gameMode != GameMode.SURVIVAL) return
        if(GuiManager.allowClick(event)) return
        if(event.click == ClickType.WINDOW_BORDER_LEFT || event.click == ClickType.WINDOW_BORDER_RIGHT || event.isShiftClick || event.clickedInventory == null
            || event.click == ClickType.NUMBER_KEY && event.hotbarButton in 0..3) {
            event.isCancelled = true
            return
        }

        if(event.clickedInventory?.type != InventoryType.PLAYER) return
        if(event.slot in 4..8) return

        event.isCancelled = true
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent){
        if(event.inventory.type == InventoryType.CHEST) return
        if(event.rawSlots.all { it in 40..44 }) return
        event.isCancelled = true
    }

    @EventHandler
    fun onItemSwap(event: PlayerSwapHandItemsEvent) {
        val player = event.player

        if(player.hasPermission("ingenia.inventory.move") || player.isOp) return
        if(player.gameMode != GameMode.ADVENTURE && player.gameMode != GameMode.SURVIVAL) return

        event.isCancelled = true
    }

    @EventHandler
    fun onItemDrop(event: PlayerDropItemEvent) {
        if(!event.player.hasPermission("ingenia.admin") && !event.player.isOp)
            event.isCancelled = true
    }

}