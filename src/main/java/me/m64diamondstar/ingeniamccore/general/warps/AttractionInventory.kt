package me.m64diamondstar.ingeniamccore.general.warps

import me.m64diamondstar.ingeniamccore.attractions.utils.Attraction
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.general.inventory.MainInventory
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.gui.Gui
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class AttractionInventory(player: IngeniaPlayer): Gui(player) {

    private val slots = HashMap<Int, Attraction>()

    override fun setDisplayName(): String {
        return Colors.format("&f\uF808金")
    }

    override fun setSize(): Int {
        return 54
    }

    override fun handleInventory(event: InventoryClickEvent) {
        if(event.slot in 0..44){
            if(slots.containsKey(event.slot)){
                getPlayer().player.teleport(slots[event.slot]?.getWarpLocation()!!)
            }
        }

        if(event.slot == 49){
            val mainInventory = MainInventory(getPlayer())
            mainInventory.open()
        }
    }

    override fun setInventoryItems() {
        for(attraction in AttractionUtils.getAllAttractions()){
            if(attraction.getWarpItem() != null && attraction.getWarpLocation() != null){
                slots[inventory.firstEmpty()] = attraction
                inventory.setItem(inventory.firstEmpty(), attraction.getWarpItem())
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