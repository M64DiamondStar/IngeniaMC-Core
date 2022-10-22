package me.m64diamondstar.ingeniamccore.general.inventory

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.gui.Gui
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class MainInventory(player: IngeniaPlayer): Gui(player) {

    override fun setDisplayName(): String {
        return ""
    }

    override fun setSize(): Int {
        return 54
    }

    override fun setInventoryItems() {
        inventory.setItem(1, ItemStack(Material.ACTIVATOR_RAIL))
    }

    override fun handleInventory(event: InventoryClickEvent) {

    }

}