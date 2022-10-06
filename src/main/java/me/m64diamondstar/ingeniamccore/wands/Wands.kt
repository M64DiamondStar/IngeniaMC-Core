package me.m64diamondstar.ingeniamccore.wands

import me.m64diamondstar.ingeniamccore.utils.Colors
import me.m64diamondstar.ingeniamccore.utils.MessageType
import me.m64diamondstar.ingeniamccore.wands.wands.*
import org.bukkit.BlockChangeDelegate
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

        if(Air(player).hasPermission()) accessibleWands.add(getAsItemStack(Air(player)))
        if(AntiGravity(player).hasPermission()) accessibleWands.add(getAsItemStack(AntiGravity(player)))
        if(BlockLauncher(player).hasPermission()) accessibleWands.add(getAsItemStack(BlockLauncher(player)))
        if(Bouncer(player).hasPermission()) accessibleWands.add(getAsItemStack(Bouncer(player)))
        if(Bush(player).hasPermission()) accessibleWands.add(getAsItemStack(Bush(player)))
        if(Cloak(player).hasPermission()) accessibleWands.add(getAsItemStack(Cloak(player)))
        if(Earth(player).hasPermission()) accessibleWands.add(getAsItemStack(Earth(player)))
        if(Fire(player).hasPermission()) accessibleWands.add(getAsItemStack(Fire(player)))
        if(Fly(player).hasPermission()) accessibleWands.add(getAsItemStack(Fly(player)))
        if(Happiness(player).hasPermission()) accessibleWands.add(getAsItemStack(Happiness(player)))
        if(HolyTomato(player).hasPermission()) accessibleWands.add(getAsItemStack(HolyTomato(player)))
        if(Launch(player).hasPermission()) accessibleWands.add(getAsItemStack(Launch(player)))
        if(Music(player).hasPermission()) accessibleWands.add(getAsItemStack(Music(player)))
        if(Sled(player).hasPermission()) accessibleWands.add(getAsItemStack(Sled(player)))
        if(SnowCannon(player).hasPermission()) accessibleWands.add(getAsItemStack(SnowCannon(player)))
        if(SnowExplosion(player).hasPermission()) accessibleWands.add(getAsItemStack(SnowExplosion(player)))
        if(Sled(player).hasPermission()) accessibleWands.add(getAsItemStack(Sled(player)))
        if(TNT(player).hasPermission()) accessibleWands.add(getAsItemStack(TNT(player)))
        if(Water(player).hasPermission()) accessibleWands.add(getAsItemStack(Water(player)))


        return accessibleWands
    }

}