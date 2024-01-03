package me.m64diamondstar.ingeniamccore.cosmetics.data

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.cosmetics.utils.CosmeticType
import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import me.m64diamondstar.ingeniamccore.utils.items.ItemDecoder
import me.m64diamondstar.ingeniamccore.utils.items.ItemEncoder
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

/**
 * @param cosmeticType which CosmeticType to edit.
 */
class CosmeticItems(cosmeticType: CosmeticType): DataConfiguration("data/items", cosmeticType.toString()) {

    /**
     * Check if an ID exists
     * @param id the ID of the cosmetic
     */
    fun exists(id: String): Boolean {
        return this.getConfig().get(id) != null
    }

    /**
     * @param id the ID to give to the item
     * @param item the of this cosmetic
     * @param override whether it should override already existing IDs
     * @return whether the operation has been successful
     */
    fun create(id: String, item: ItemStack, override: Boolean): Boolean {
        if(this.getConfig().get(id) != null && !override)
            return false // Operation was cancelled because item already existed
        this.getConfig().set("$id.Data", item.serialize())
        this.save()
        return true // Operation has been completed successfully
    }

    /**
     * Change the item of a cosmetic
     * @param id the ID of the cosmetic item
     * @param item the new item it should receive
     */
    fun setItem(id: String, item: ItemStack) {
        create(id, item, true)
    }

    /**
     * Get the ItemStack of a cosmetic item
     * @param id the ID of the cosmetic item
     */
    fun getItem(id: String?): ItemStack? {
        val item = ItemStack.deserialize(getConfig().getConfigurationSection("$id.Data")?.getValues(true) ?: return null)
        return applyID(item, id ?: return null)
    }

    /**
     * Delete a cosmetic item
     * @param id the ID of the cosmetic
     */
    fun delete(id: String){
        this.getConfig().set(id, null)
        this.save()
    }

    /**
     * Get all the current IDs in this cosmetic file
     * @return the list of the IDs
     */
    fun getAllIDs(): List<String>{
        val list = ArrayList<String>()
        this.getConfig().getKeys(false).forEach { list.add(it) }
        return list
    }

    /**
     * Gets the ID of an item
     * @param item the item
     * @return the ID
     */
    fun getID(item: ItemStack): String?{
        val meta = item.itemMeta!!
        return meta.persistentDataContainer.get(NamespacedKey(IngeniaMC.plugin, "cosmetic"), PersistentDataType.STRING)
    }

    /**
     * Applies an ID to an item
     * @param item the item
     * @param id the ID
     */
    private fun applyID(item: ItemStack, id: String): ItemStack?{
        val meta = item.itemMeta ?: return null
        meta.persistentDataContainer.set(NamespacedKey(IngeniaMC.plugin, "cosmetic"), PersistentDataType.STRING, id)
        item.itemMeta = meta
        return item
    }

}