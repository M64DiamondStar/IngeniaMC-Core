package me.m64diamondstar.ingeniamccore.protect.listeners

import me.m64diamondstar.ingeniamccore.utils.entities.EntityUtils
import me.m64diamondstar.ingeniamccore.warps.WarpUtils
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDismountEvent

class EntityDismountListener: Listener {

    @EventHandler (priority = EventPriority.HIGH)
    fun onEntityDismount(event: EntityDismountEvent){
        if(event.entity !is Player) return
        val player = event.entity as Player
        val entity = event.dismounted

        if(!player.isOnline)
            return

        if(EntityUtils.isLocked(entity)) {
            player.teleport(WarpUtils.getNearestLocation(player))
        }
    }

}