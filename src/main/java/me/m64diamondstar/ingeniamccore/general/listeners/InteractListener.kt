package me.m64diamondstar.ingeniamccore.general.listeners

import me.m64diamondstar.ingeniamccore.general.inventory.MainInventory
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.items.ItemChecker
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class InteractListener: Listener {

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent){
        val player = event.player

        if(event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return

        val itemChecker = ItemChecker(event.item)

        if(itemChecker.isMainInventoryItem()) {
            val mainInventory = MainInventory(IngeniaPlayer(player))
            mainInventory.open()
        }
    }
}