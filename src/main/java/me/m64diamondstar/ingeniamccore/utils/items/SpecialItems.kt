package me.m64diamondstar.ingeniamccore.utils.items

import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object SpecialItems {

    val blankItem: ItemStack
        get() {
            val item = ItemStack(Material.FEATHER)
            val meta = item.itemMeta
            meta!!.setDisplayName(Colors.format("&0"))
            meta.setCustomModelData(1)
            item.itemMeta = meta
            return item
        }

}