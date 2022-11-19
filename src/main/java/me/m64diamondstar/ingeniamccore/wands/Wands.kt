package me.m64diamondstar.ingeniamccore.wands

import com.mojang.datafixers.kinds.IdF
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.wands.wands.*
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
    fun getAccessibleWands(player: Player): List<ItemStack>{

        val accessibleWands = ArrayList<ItemStack>()

        if(Air().hasPermission(player)) accessibleWands.add(getAsItemStack(Air()))
        if(AntiGravity().hasPermission(player)) accessibleWands.add(getAsItemStack(AntiGravity()))
        if(BlockLauncher().hasPermission(player)) accessibleWands.add(getAsItemStack(BlockLauncher()))
        if(Bouncer().hasPermission(player)) accessibleWands.add(getAsItemStack(Bouncer()))
        if(Bush().hasPermission(player)) accessibleWands.add(getAsItemStack(Bush()))
        if(Cloak().hasPermission(player)) accessibleWands.add(getAsItemStack(Cloak()))
        if(Earth().hasPermission(player)) accessibleWands.add(getAsItemStack(Earth()))
        if(Fire().hasPermission(player)) accessibleWands.add(getAsItemStack(Fire()))
        if(Fly().hasPermission(player)) accessibleWands.add(getAsItemStack(Fly()))
        if(Grapple().hasPermission(player)) accessibleWands.add(getAsItemStack(Grapple()))
        if(Happiness().hasPermission(player)) accessibleWands.add(getAsItemStack(Happiness()))
        if(HolyTomato().hasPermission(player)) accessibleWands.add(getAsItemStack(HolyTomato()))
        if(Launch().hasPermission(player)) accessibleWands.add(getAsItemStack(Launch()))
        if(Music().hasPermission(player)) accessibleWands.add(getAsItemStack(Music()))
        if(Sled().hasPermission(player)) accessibleWands.add(getAsItemStack(Sled()))
        if(SnowCannon().hasPermission(player)) accessibleWands.add(getAsItemStack(SnowCannon()))
        if(SnowExplosion().hasPermission(player)) accessibleWands.add(getAsItemStack(SnowExplosion()))
        if(Speed().hasPermission(player)) accessibleWands.add(getAsItemStack(Speed()))
        if(TNT().hasPermission(player)) accessibleWands.add(getAsItemStack(TNT()))
        if(Water().hasPermission(player)) accessibleWands.add(getAsItemStack(Water()))

        return accessibleWands
    }


    private fun getWandDisplayName(wand: Wand): String{
        return wand.getDisplayName()
    }

    fun getWandDisplayName(wand: String): String{
        if(wand.equals("air", ignoreCase = true)) return getWandDisplayName(Air())
        if(wand.equals("antigravity", ignoreCase = true)) return getWandDisplayName(AntiGravity())
        if(wand.equals("blocklauncher", ignoreCase = true)) return getWandDisplayName(BlockLauncher())
        if(wand.equals("bouncer", ignoreCase = true)) return getWandDisplayName(Bouncer())
        if(wand.equals("bush", ignoreCase = true)) return getWandDisplayName(Bush())
        if(wand.equals("cloak", ignoreCase = true)) return getWandDisplayName(Cloak())
        if(wand.equals("earth", ignoreCase = true)) return getWandDisplayName(Earth())
        if(wand.equals("fire", ignoreCase = true)) return getWandDisplayName(Fire())
        if(wand.equals("fly", ignoreCase = true)) return getWandDisplayName(Fly())
        if(wand.equals("grapple", ignoreCase = true)) return getWandDisplayName(Grapple())
        if(wand.equals("happiness", ignoreCase = true)) return getWandDisplayName(Happiness())
        if(wand.equals("holytomato", ignoreCase = true)) return getWandDisplayName(HolyTomato())
        if(wand.equals("launch", ignoreCase = true)) return getWandDisplayName(Launch())
        if(wand.equals("music", ignoreCase = true)) return getWandDisplayName(Music())
        if(wand.equals("sled", ignoreCase = true)) return getWandDisplayName(Sled())
        if(wand.equals("snowcannon", ignoreCase = true)) return getWandDisplayName(SnowCannon())
        if(wand.equals("snowexplosion", ignoreCase = true)) return getWandDisplayName(SnowExplosion())
        if(wand.equals("speed", ignoreCase = true)) return getWandDisplayName(Speed())
        if(wand.equals("tnt", ignoreCase = true)) return getWandDisplayName(TNT())
        if(wand.equals("water", ignoreCase = true)) return getWandDisplayName(Water())
        return "null"
    }

}