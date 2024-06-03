package me.m64diamondstar.ingeniamccore.utils.items

import me.m64diamondstar.ingeniamccore.IngeniaMC
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class ItemChecker(private val itemStack: ItemStack?) {

    fun isMainMenu(): Boolean {
        if (itemStack == null) return false
        if (itemStack.type != Material.NETHER_STAR) return false
        if (!itemStack.hasItemMeta()) return false
        if (!itemStack.itemMeta!!.persistentDataContainer.has(NamespacedKey(IngeniaMC.plugin, "menu-item"), PersistentDataType.STRING)) return false
        if (itemStack.itemMeta!!.persistentDataContainer.get(NamespacedKey(IngeniaMC.plugin, "menu-item"), PersistentDataType.STRING) != "main") return false
        return true
    }

    fun isRidesMenu(): Boolean {
        if (itemStack == null) return false
        if (itemStack.type != Material.MINECART) return false
        if (!itemStack.hasItemMeta()) return false
        if (!itemStack.itemMeta!!.persistentDataContainer.has(NamespacedKey(IngeniaMC.plugin, "menu-item"), PersistentDataType.STRING)) return false
        if (itemStack.itemMeta!!.persistentDataContainer.get(NamespacedKey(IngeniaMC.plugin, "menu-item"), PersistentDataType.STRING) != "rides") return false
        return true
    }

    fun isShopsMenu(): Boolean {
        if (itemStack == null) return false
        if (itemStack.type != Material.ENDER_CHEST) return false
        if (!itemStack.hasItemMeta()) return false
        if (!itemStack.itemMeta!!.persistentDataContainer.has(NamespacedKey(IngeniaMC.plugin, "menu-item"), PersistentDataType.STRING)) return false
        if (itemStack.itemMeta!!.persistentDataContainer.get(NamespacedKey(IngeniaMC.plugin, "menu-item"), PersistentDataType.STRING) != "shops") return false
        return true
    }

}
