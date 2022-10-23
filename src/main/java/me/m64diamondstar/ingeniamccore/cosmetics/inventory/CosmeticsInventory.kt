package me.m64diamondstar.ingeniamccore.cosmetics.inventory

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.gui.Gui
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

class CosmeticsInventory(private var player: Player, private var selected: String): Gui(IngeniaPlayer(player)) {

    private val freeSlots = Array(21) {i -> if(i in  0..6) i + 19 else if(i in 7..13) i + 21 else i + 23}

    override fun setDisplayName(): String {
        return Colors.format("\uF808&f田\uF81C\uF81A\uF819\uF811$selected")
    }

    override fun setSize(): Int {
        return 54
    }

    override fun handleInventory(event: InventoryClickEvent) {
        val player = event.whoClicked as Player
        val ingeniaPlayer = IngeniaPlayer(player)

        if(event.slot == -999 || !event.view.title.contains("田")) return


        if(event.slot in 1..7 && event.slot != 4) {
            var inventory = CosmeticsInventory(player, "國")

            //All the different options

            if (event.slot == 1)
                inventory = CosmeticsInventory(player, "國") //Hats
            if (event.slot == 2)
                inventory = CosmeticsInventory(player, "因") //Shirts因
            if (event.slot == 3)
                inventory = CosmeticsInventory(player, "圖") //Wands圖
            if (event.slot == 5)
                inventory = CosmeticsInventory(player, "果") //Balloons果
            if (event.slot == 6)
                inventory = CosmeticsInventory(player, "四") //Pants四
            if (event.slot == 7)
                inventory = CosmeticsInventory(player, "界") //Boots界

            inventory.open()
        }

        //What happens when wand is used?

        if(event.view.title.contains("圖")) {
            if (freeSlots.contains(event.slot) && event.currentItem != null) {
                ingeniaPlayer.setWand(event.currentItem)
                inventory.setItem(3, event.currentItem)
            }
        }
    }

    override fun setInventoryItems() {
        if(player.inventory.getItem(5) != null
            && player.inventory.getItem(5)!!.type == Material.BLAZE_ROD
            && player.inventory.getItem(5)!!.hasItemMeta()){
            inventory.setItem(3, player.inventory.getItem(5))
        }

        if(selected == "圖")
            addWands()
    }

    /**
     * Run when wand gui is chosen.
     **/
    private fun addWands(){
        val ingeniaPlayer = IngeniaPlayer(player)

        if(ingeniaPlayer.wands.size > freeSlots.size)
            for(i in 0..20){
                inventory.setItem(freeSlots[i], IngeniaPlayer(player).wands[i])
            }
        else
            for(i in 0 until IngeniaPlayer(player).wands.size){
                inventory.setItem(freeSlots[i], IngeniaPlayer(player).wands[i])
            }
    }


}