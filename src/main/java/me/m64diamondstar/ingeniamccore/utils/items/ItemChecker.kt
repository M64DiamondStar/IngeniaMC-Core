package me.m64diamondstar.ingeniamccore.utils.items

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemChecker(itemStack: ItemStack?) {

    private val itemStack: ItemStack?

    init {
        this.itemStack = itemStack
    }

    fun isMainInventoryItem(): Boolean {
        if (itemStack == null) return false
        if (itemStack.type != Material.NETHER_STAR) return false
        if (!itemStack.hasItemMeta()) return false
        if (!itemStack.itemMeta?.displayName!!.contains("IngeniaMC")) return false

        return true
    }

}
