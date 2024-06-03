package me.m64diamondstar.ingeniamccore.utils.gui

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent

abstract class InventoryHandler(val ingeniaPlayer: IngeniaPlayer) {

    abstract fun setDisplayName(): Component
    abstract fun setSize(): Int
    abstract fun shouldCancel(): Boolean
    abstract fun onClick(event: InventoryClickEvent)
    abstract fun onDrag(event: InventoryDragEvent)
    abstract fun onOpen(event: InventoryOpenEvent)
    abstract fun onClose(event: InventoryCloseEvent)

    fun getPlayer(): IngeniaPlayer = ingeniaPlayer

    fun open(){
        val inventory = Bukkit.createInventory(null, setSize(), setDisplayName())
        GuiManager.registerHandledInventory(inventory, this)
        ingeniaPlayer.openInventory(inventory)
    }
}