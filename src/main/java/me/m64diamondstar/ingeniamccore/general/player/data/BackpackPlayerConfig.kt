package me.m64diamondstar.ingeniamccore.general.player.data

import me.m64diamondstar.ingeniamccore.cosmetics.data.BackpackItems
import me.m64diamondstar.ingeniamccore.cosmetics.utils.BackpackItemType
import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class BackpackPlayerConfig(val player: Player): DataConfiguration("data/backpack-user", player.uniqueId.toString()) {

    /**
     * Gets the items in a specific page.
     * @return the ItemStack for each slot
     */
    fun getPage(page: Int): MutableMap<Int, ItemStack> {
        val map = mutableMapOf<Int, ItemStack>()
        for(i in 0..44){
            if(getItem(page, i) != null)
                map[i] = getItem(page, i)!!
        }
        return map
    }

    /**
     * Sets the items in a specific page.
     * @param map the ItemStack for each slot
     */
    fun setPage(page: Int, map: MutableMap<Int, ItemStack>) {
        for(i in 0..44){
            if(map[i] != null)
                setItem(page, i, map[i]!!)
            else
                clearItem(page, i)
        }
        this.save()
    }

    fun getItem(page: Int, slot: Int): ItemStack? {
        return this.getConfig().getItemStack("pages.$page.$slot")
    }

    fun setItem(page: Int, slot: Int, item: ItemStack) {
        this.getConfig().set("pages.$page.$slot", item)
        this.save()
    }

    fun clearItem(page: Int, slot: Int) {
        this.getConfig().set("pages.$page.$slot", null)
        this.save()
    }

    fun addItem(item: ItemStack): Boolean {
        for(page in 1..9) {
            for (slot in 0..44) {
                if (getItem(page, slot) == null) {
                    setItem(page, slot, item)
                    return true
                }
            }
        }
        return false
    }

    fun addItem(backpackItemType: BackpackItemType, id: String, amount: Int){
        val backpackItems = BackpackItems(backpackItemType)
        val item = backpackItems.getItem(id) ?: return
        item.amount = amount
        addItem(item)
    }

    fun isFull(): Boolean {
        for(page in 1..9) {
            for (slot in 0..44) {
                if (getItem(page, slot) == null) {
                    return false
                }
            }
        }
        return true
    }

}