package me.m64diamondstar.ingeniamccore.cosmetics.inventory

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class CosmeticsInventory(private var player: Player) {

    private lateinit var inv: Inventory
    private val freeSlots = Array(21) {i -> if(i in  0..6) i + 19 else if(i in 7..13) i + 21 else i + 23}

    /**
     * Open the cosmetic inventory for a player.
     */
    fun openInventory(){
        openInventory("國")
    }

    /**
     * Open the cosmetic inventory for a player on a specific tab.
     */
    fun openInventory(selected: String){

        inv = Bukkit.createInventory(null, 54, Colors.format("\uF808&f田\uF81C\uF81A\uF819\uF811$selected"))

        if(player.inventory.getItem(5) != null
            && player.inventory.getItem(5)!!.type == Material.BLAZE_ROD
            && player.inventory.getItem(5)!!.hasItemMeta()){
            inv.setItem(3, player.inventory.getItem(5))
        }

        if(selected == "圖")
            addWands()

        player.openInventory(inv)
    }

    /**
     * Run when wand gui is chosen.
     **/
    private fun addWands(){
        val ingeniaPlayer = IngeniaPlayer(player)

        if(ingeniaPlayer.wands.size > freeSlots.size)
            for(i in 0..20){
                inv.setItem(freeSlots[i], IngeniaPlayer(player).wands[i])
            }
        else
            for(i in 0 until IngeniaPlayer(player).wands.size){
                inv.setItem(freeSlots[i], IngeniaPlayer(player).wands[i])
            }
    }



}