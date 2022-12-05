package me.m64diamondstar.ingeniamccore.general.listeners.protection

import me.m64diamondstar.ingeniamccore.general.listeners.protection.utils.BlockDataConfig
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

class InteractListener: Listener {

    @EventHandler
    fun onInteract(event: PlayerInteractEvent){
        val player = event.player

        //See if block has to be checked
        if(event.action != Action.RIGHT_CLICK_BLOCK && event.action != Action.PHYSICAL) return
        if(event.clickedBlock == null) return
        if(player.hasPermission("ingenia.admin") || player.isOp) return

        if(event.clickedBlock!!.type == Material.TRIPWIRE){
            event.isCancelled = true
            return
        }

        if(event.clickedBlock!!.type.toString().contains("PRESSURE_PLATE") || //is correct block?
            event.clickedBlock!!.type.toString().contains("BUTTON") ||
            event.clickedBlock!!.type.toString().contains("TRAPDOOR") ||
            event.clickedBlock!!.type.toString().contains("BUTTON") ||
            event.clickedBlock!!.type.toString().contains("LEVER")){

            val blockDataConfig = BlockDataConfig()
            if(blockDataConfig.isApprovedBlock(event.clickedBlock!!.location)){
                event.isCancelled = false
                return
            }

        }

        event.isCancelled = true

    }

    @EventHandler
    fun onEntityInteract(event: PlayerInteractEntityEvent) {
        val player = event.player
        val entity = event.rightClicked

        if(player.hasPermission("ingenia.admin") || player.isOp) return
        if(!(entity.type == EntityType.ARMOR_STAND || entity.type == EntityType.ITEM_FRAME || entity.type == EntityType.GLOW_ITEM_FRAME)) return

        event.isCancelled = true
    }

}