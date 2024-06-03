package me.m64diamondstar.ingeniamccore.utils.gui

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory

object GuiManager {

    private val activeInventories = HashMap<Inventory, InventoryHandler>()

    fun registerHandledInventory(inventory: Inventory, handler: InventoryHandler){
        activeInventories[inventory] = handler
    }

    private fun unregisterHandledInventory(inventory: Inventory){
        activeInventories.remove(inventory)
    }

    fun handleClick(event: InventoryClickEvent){
        if(activeInventories.containsKey(event.inventory)){
            if(activeInventories[event.inventory]!!.shouldCancel())
                event.isCancelled = true
            activeInventories[event.inventory]!!.onClick(event)
        }
    }

    fun handleDrag(event: InventoryDragEvent){
        if(activeInventories.containsKey(event.inventory)){
            activeInventories[event.inventory]!!.onDrag(event)
        }
    }

    fun handleOpen(event: InventoryOpenEvent){
        if(activeInventories.containsKey(event.inventory)){
            activeInventories[event.inventory]!!.onOpen(event)
        }
    }

    fun handleClose(event: InventoryCloseEvent){
        if(activeInventories.containsKey(event.inventory)){
            activeInventories[event.inventory]!!.onClose(event)
            unregisterHandledInventory(event.inventory)
        }
    }

    fun allowClick(event: InventoryClickEvent): Boolean{
        return activeInventories.containsKey(event.inventory) && !activeInventories[event.inventory]!!.shouldCancel()
    }

}