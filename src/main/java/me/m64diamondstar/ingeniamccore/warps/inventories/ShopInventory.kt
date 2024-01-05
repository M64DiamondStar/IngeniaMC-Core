package me.m64diamondstar.ingeniamccore.warps.inventories

import me.m64diamondstar.ingeniamccore.general.inventory.MainInventory
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.protect.FeatureManager
import me.m64diamondstar.ingeniamccore.protect.FeatureType
import me.m64diamondstar.ingeniamccore.utils.gui.Gui
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import me.m64diamondstar.ingeniamccore.warps.WarpManager
import me.m64diamondstar.ingeniamccore.warps.WarpType
import me.m64diamondstar.ingeniamccore.warps.WarpUtils
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class ShopInventory(player: IngeniaPlayer): Gui(player) {

    override fun setDisplayName(): String {
        return Colors.format("&f\uF808港")
    }

    override fun setSize(): Int {
        return 54
    }

    override fun handleInventory(event: InventoryClickEvent) {
        if(event.slot in 0..44){
            if(event.currentItem != null && WarpUtils.getIDFromItem(event.currentItem!!) != null){
                val featureManager = FeatureManager()
                if(!featureManager.isFeatureEnabled(FeatureType.WARPS) && !getPlayer().player.hasPermission("ingenia.admin")){
                    getPlayer().player.sendMessage(Messages.featureDisabled())
                    return
                }

                val warpManager = WarpManager()
                val id = WarpUtils.getIDFromItem(event.currentItem!!)!!
                val location = warpManager.getWarpLocation(id) ?: return
                getPlayer().player.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN)
                getPlayer().player.spawnParticle(Particle.PORTAL, location.clone().add(0.0, 1.0, 0.0), 20, 0.4, 0.8, 0.4, 0.0)
            }
        }

        if(event.slot == 49){
            val mainInventory = MainInventory(getPlayer())
            mainInventory.open()
        }
    }

    override fun setInventoryItems() {
        val warpManager = WarpManager()
        warpManager.getAllIDs(WarpType.SHOP).forEach {
            if(warpManager.getWarpItem(it) != null){
                inventory.addItem(warpManager.getWarpItem(it)!!)
            }
        }

        val transparentItem = ItemStack(Material.FEATHER)
        val transparentMeta = transparentItem.itemMeta as ItemMeta
        transparentMeta.setDisplayName(Colors.format(MessageType.ERROR + "&lGo Back"))
        transparentMeta.lore = listOf(Colors.format("${MessageType.LORE}Click here to go back to the main menu."))
        transparentMeta.setCustomModelData(1)

        transparentItem.itemMeta = transparentMeta

        inventory.setItem(49, transparentItem)
    }
}