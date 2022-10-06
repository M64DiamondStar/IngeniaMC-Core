package me.m64diamondstar.ingeniamccore.cosmetics

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class CosmeticsListener : Listener {

    private val freeSlots = Array(21) {i -> if(i in  0..6) i + 19 else if(i in 7..13) i + 21 else i + 23}

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent){
        val player = event.whoClicked as Player
        val ingeniaPlayer = IngeniaPlayer(player)

        if(event.slot == -999 || !event.view.title.contains("田")) return

        event.isCancelled = true

        val inventory = CosmeticsInventory(player)

        //All the different options

        if(event.slot == 1)
            inventory.openInventory("國") //Hats
        if(event.slot == 2)
            inventory.openInventory("因") //Shirts
        if(event.slot == 3)
            inventory.openInventory("圖") //Wands
        if(event.slot == 5)
            inventory.openInventory("果") //Balloons
        if(event.slot == 6)
            inventory.openInventory("四") //Pants
        if(event.slot == 7)
            inventory.openInventory("界") //Boots



        //What happens when wand is used?

        if(event.view.title.contains("圖")) {
            if (freeSlots.contains(event.slot) && event.currentItem != null) {
                ingeniaPlayer.setWand(event.currentItem)
            }
        }

    }

}