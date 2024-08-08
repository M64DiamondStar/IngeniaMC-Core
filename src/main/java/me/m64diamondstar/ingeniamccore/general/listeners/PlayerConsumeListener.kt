package me.m64diamondstar.ingeniamccore.general.listeners

import me.m64diamondstar.ingeniamccore.cosmetics.data.ConsumableConvertData
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack

class PlayerConsumeListener: Listener {
    
    @EventHandler
    fun onConsume(event: PlayerItemConsumeEvent){
        val consumableConvertData = ConsumableConvertData()
        val newItem = consumableConvertData.getConsumedItem(event.item.type, event.item.itemMeta.customModelData) ?: ItemStack(Material.AIR)

        event.replacement = newItem
    }

}