package me.m64diamondstar.ingeniamccore.general.listeners

import me.m64diamondstar.ingeniamccore.general.inventory.MainInventory
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.protect.FeatureManager
import me.m64diamondstar.ingeniamccore.protect.FeatureType
import me.m64diamondstar.ingeniamccore.utils.items.ItemChecker
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import me.m64diamondstar.ingeniamccore.warps.inventories.AttractionInventory
import me.m64diamondstar.ingeniamccore.warps.inventories.ShopInventory
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

        val featureManager = FeatureManager()
        if(!featureManager.isFeatureEnabled(FeatureType.MENU) && !player.hasPermission("ingenia.admin")){
            player.sendMessage(Messages.featureDisabled())
            return
        }

        if(itemChecker.isMainMenu()) {
            val mainInventory = MainInventory(IngeniaPlayer(player), 0)
            mainInventory.open()
        }

        if(itemChecker.isRidesMenu()) {
            val ridesInventory = AttractionInventory(IngeniaPlayer(player))
            ridesInventory.open()
        }

        if(itemChecker.isShopsMenu()) {
            val shopInventory = ShopInventory(IngeniaPlayer(player))
            shopInventory.open()
        }
    }
}