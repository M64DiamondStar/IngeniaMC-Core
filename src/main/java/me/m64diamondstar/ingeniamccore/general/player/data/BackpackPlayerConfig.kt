package me.m64diamondstar.ingeniamccore.general.player.data

import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class BackpackPlayerConfig(val player: Player): DataConfiguration("data/backpack-user", player.uniqueId.toString()) {

    fun addItem(itemStack: ItemStack){

    }

}