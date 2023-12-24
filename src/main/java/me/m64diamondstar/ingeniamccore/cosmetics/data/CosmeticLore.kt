package me.m64diamondstar.ingeniamccore.cosmetics.data

import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.inventory.ItemStack

object CosmeticLore {

    fun getAsSelect(item: ItemStack): ItemStack{
        val meta = item.itemMeta!!
        val lore = meta.lore!!
        lore.addAll(listOf(" ", Colors.format(MessageType.SUCCESS + "Click to select.")))
        meta.lore = lore
        item.itemMeta = meta
        return item
    }

    fun getAsDeselect(item: ItemStack): ItemStack{
        val meta = item.itemMeta!!
        val lore = meta.lore!!
        lore.addAll(listOf(" ", Colors.format(MessageType.ERROR + "Click to deselect.")))
        meta.lore = lore
        item.itemMeta = meta
        return item
    }

}