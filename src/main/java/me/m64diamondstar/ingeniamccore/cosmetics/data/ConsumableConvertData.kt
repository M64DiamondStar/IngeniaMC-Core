package me.m64diamondstar.ingeniamccore.cosmetics.data

import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ConsumableConvertData: DataConfiguration("data", "consumable_convert.yml") {

    fun removeConsumable(oldItem: Material, oldData: Int) {
        getConfig().set("Consumable.$oldItem.$oldData", null)
        save()
    }

    fun setConsumedItem(oldItem: Material, oldData: Int, itemStack: ItemStack) {
        getConfig().set("Consumable.$oldItem.$oldData.ConsumedItem", itemStack)
        save()
    }

    fun getConsumedItem(oldItem: Material, oldData: Int): ItemStack? {
        return getConfig().getItemStack("Consumable.$oldItem.$oldData.ConsumedItem")
    }

}