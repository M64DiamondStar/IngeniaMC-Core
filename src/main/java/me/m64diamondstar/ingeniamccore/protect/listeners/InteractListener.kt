package me.m64diamondstar.ingeniamccore.protect.listeners

import me.m64diamondstar.ingeniamccore.protect.utils.BlockDataConfig
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class InteractListener: Listener {

    @EventHandler
    fun onInteract(event: PlayerInteractEvent){
        val player = event.player

        //Always cancel tripwire (used for custom models like plants)
        if (event.action == Action.PHYSICAL) {
            if (event.clickedBlock?.type == Material.FARMLAND) {
                event.isCancelled = true
            }
        }

        //See if block has to be checked
        if(event.action != Action.RIGHT_CLICK_BLOCK) return
        if(event.clickedBlock == null) return
        if(player.hasPermission("ingenia.admin") || player.isOp) return

        if(event.clickedBlock!!.type.toString().contains("PRESSURE_PLATE") || //is correct block?
            event.clickedBlock!!.type.toString().contains("BUTTON") ||
            event.clickedBlock!!.type.toString().contains("TRAPDOOR") ||
            event.clickedBlock!!.type.toString().contains("BUTTON") ||
            event.clickedBlock!!.type.toString().contains("LEVER")){

            val blockDataConfig = BlockDataConfig()
            if(blockDataConfig.isApprovedBlock(event.clickedBlock!!.location)){ //See if the block is in the approved block list
                event.isCancelled = false
                return
            }

        }

        event.isCancelled = true

    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent){
        val player = event.player
        if(event.action != Action.RIGHT_CLICK_BLOCK) return
        if(event.clickedBlock == null || event.clickedBlock!!.type.isAir) return
        if(!event.clickedBlock!!.type.toString().contains("SIGN")) return
        if(player.hasPermission("ingenia.team") || player.hasPermission("ingenia.admin") || player.isOp) return

        event.isCancelled = true
    }

    @EventHandler
    fun onArmorChange(event: PlayerInteractEvent){
        if(event.item == null) return
        if(event.player.hasPermission("ingenia.admin") || event.player.isOp) return
        if(!event.item!!.type.toString().contains("HELMET")
            && !event.item!!.type.toString().contains("CHESTPLATE")
            && !event.item!!.type.toString().contains("LEGGINGS")
            && !event.item!!.type.toString().contains("BOOTS")) return
        event.isCancelled = true
    }

}