package me.m64diamondstar.ingeniamccore.general.warps

import me.m64diamondstar.ingeniamccore.general.inventory.MainInventory
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.gui.Gui
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
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
        if(event.slot == 49){
            val mainInventory = MainInventory(getPlayer())
            mainInventory.open()
        }
    }

    override fun setInventoryItems() {
        val transparentItem = ItemStack(Material.FEATHER)
        val transparentMeta = transparentItem.itemMeta as ItemMeta
        transparentMeta.setDisplayName(Colors.format(MessageType.ERROR + "&lGo Back"))
        transparentMeta.lore = listOf(Colors.format("${MessageType.LORE}Click here to go back to the main menu."))
        transparentMeta.setCustomModelData(1)

        transparentItem.itemMeta = transparentMeta

        inventory.setItem(49, transparentItem)
    }
}