package me.m64diamondstar.ingeniamccore.utils.gui

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent

class GuiListener : Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onInventoryClick(event: InventoryClickEvent){
        GuiManager.handleClick(event)
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent){
        GuiManager.handleDrag(event)
    }

    @EventHandler
    fun onInventoryOpen(event: InventoryOpenEvent){
        GuiManager.handleOpen(event)
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent){
        GuiManager.handleClose(event)
    }
}