package me.m64diamondstar.ingeniamccore.wands.utils

import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object Wands {

    @JvmStatic
    fun getAsItemStack(wand: Wand): ItemStack{
        val item = ItemStack(Material.BLAZE_ROD)
        val meta = item.itemMeta
        meta?.setDisplayName(wand.getDisplayName())
        val lore = ArrayList<String>()
        lore.add(Colors.format(MessageType.LORE + "Right Click to use this wand!"))
        meta?.lore = lore
        meta?.setCustomModelData(wand.getCustomModelData())

        item.itemMeta = meta

        return item
    }

    @JvmStatic
    fun getAsItemStack(wand: String): ItemStack?{
        if(WandRegistry.getWand(wand) == null) return null

        val item = ItemStack(Material.BLAZE_ROD)
        val meta = item.itemMeta
        meta?.setDisplayName(getWandDisplayName(wand))
        val lore = ArrayList<String>()
        lore.add(Colors.format(MessageType.LORE + "Right Click to use this wand!"))
        meta?.lore = lore
        meta?.setCustomModelData(WandRegistry.getWand(wand)?.getCustomModelData())

        item.itemMeta = meta

        return item
    }

    @JvmStatic
    fun getAccessibleWands(player: Player): List<ItemStack>{
        val accessibleWands = ArrayList<ItemStack>()

        WandRegistry.getWands().forEach {
            if(player.hasPermission("ingeniawands.${it.key}"))
                accessibleWands.add(getAsItemStack(it.value))
        }
        return accessibleWands
    }


    private fun getWandDisplayName(wand: Wand): String{
        return wand.getDisplayName()
    }

    fun getWandDisplayName(wand: String): String?{
        return WandRegistry.getWand(wand)?.getDisplayName()
    }

}