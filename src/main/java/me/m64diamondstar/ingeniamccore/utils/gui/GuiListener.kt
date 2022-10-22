package me.m64diamondstar.ingeniamccore.utils.gui

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class GuiListener : Listener {

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent){
        val holder = e.inventory.holder
        if(holder is Gui) {
            e.isCancelled = true
            if (e.currentItem == null || e.slot == -999) return
            holder.handleInventory(e)
        }
    }
}