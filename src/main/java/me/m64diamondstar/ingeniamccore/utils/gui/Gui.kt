package me.m64diamondstar.ingeniamccore.utils.gui

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder


/**
    Defines all GUIs in this plugin
 **/
abstract class Gui(ingeniaPlayer: IngeniaPlayer) : InventoryHolder{

    private var ingeniaPlayer: IngeniaPlayer
    private lateinit var inventory: Inventory

    init {
        this.ingeniaPlayer = ingeniaPlayer
    }

    fun getPlayer(): IngeniaPlayer{
        return this.ingeniaPlayer
    }

    override fun getInventory(): Inventory{
        return this.inventory
    }

    abstract fun setDisplayName(): String

    abstract fun setSize(): Int

    abstract fun handleInventory(e: InventoryClickEvent)

    abstract fun setInventoryItems()

    fun open(){
        this.inventory = Bukkit.createInventory(this, setSize(), setDisplayName())
        setInventoryItems()
        ingeniaPlayer.openInventory(inventory)
    }


}