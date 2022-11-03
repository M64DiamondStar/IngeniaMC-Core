package me.m64diamondstar.ingeniamccore.general.warps

import me.m64diamondstar.ingeniamccore.attractions.utils.Attraction
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.gui.Gui
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import org.bukkit.event.inventory.InventoryClickEvent

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
    }

    override fun setInventoryItems() {
        for(attraction in AttractionUtils.getAllAttractions()){
            if(attraction.getWarpItem() != null && attraction.getWarpLocation() != null){
                slots.put(inventory.firstEmpty(), attraction)
                inventory.setItem(inventory.firstEmpty(), attraction.getWarpItem())
            }
        }
    }
}