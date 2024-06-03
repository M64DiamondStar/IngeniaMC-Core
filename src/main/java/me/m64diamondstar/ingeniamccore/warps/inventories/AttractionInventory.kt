package me.m64diamondstar.ingeniamccore.warps.inventories

import me.m64diamondstar.ingeniamccore.general.inventory.MainInventory
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.protect.FeatureManager
import me.m64diamondstar.ingeniamccore.protect.FeatureType
import me.m64diamondstar.ingeniamccore.utils.gui.InventoryHandler
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import me.m64diamondstar.ingeniamccore.warps.WarpManager
import me.m64diamondstar.ingeniamccore.warps.WarpType
import me.m64diamondstar.ingeniamccore.warps.WarpUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class AttractionInventory(player: IngeniaPlayer): InventoryHandler(player) {

    override fun setDisplayName(): Component {
        return Component.text("\uF808金").color(TextColor.color(255, 255, 255))
    }

    override fun setSize(): Int {
        return 54
    }

    override fun shouldCancel(): Boolean {
        return true
    }

    override fun onClick(event: InventoryClickEvent) {
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
                getPlayer().player.spawnParticle(Particle.PORTAL, location.add(0.0, 1.0, 0.0), 20, 0.4, 0.8, 0.4, 0.0)
            }
        }

        if(event.slot == 49){
            val mainInventory = MainInventory(getPlayer(), 0)
            mainInventory.open()
        }
    }

    override fun onDrag(event: InventoryDragEvent) {

    }

    override fun onOpen(event: InventoryOpenEvent) {
        val inventory = event.inventory
        val warpManager = WarpManager()
        warpManager.getAllIDs(WarpType.RIDE).forEach {
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

    override fun onClose(event: InventoryCloseEvent) {}
}