package me.m64diamondstar.ingeniamccore.protect.listeners

import me.m64diamondstar.ingeniamccore.utils.entities.EntityUtils
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.spigotmc.event.entity.EntityDismountEvent

class EntityDismountListener: Listener {

    @EventHandler (priority = EventPriority.HIGH)
    fun onEntityDismount(event: EntityDismountEvent){
        if(event.entity !is Player) return
        val entity = event.dismounted

        if(!(event.entity as Player).isOnline) {
            event.isCancelled = false
            return
        }
        if(EntityUtils.isLocked(entity))
            event.isCancelled = true
    }

}