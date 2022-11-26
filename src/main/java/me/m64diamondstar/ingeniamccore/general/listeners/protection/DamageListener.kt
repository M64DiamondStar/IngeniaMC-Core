package me.m64diamondstar.ingeniamccore.general.listeners.protection

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class DamageListener: Listener {

    @EventHandler
    fun onEntityDamage(event: EntityDamageByEntityEvent){
        if(event.damager.type != EntityType.PLAYER) return
        val player = event.damager as Player

        if(player.hasPermission("ingenia.admin") || player.isOp) return
        event.isCancelled = true

        if(event.entity.type != EntityType.PLAYER) return
        val damagedPlayer = event.entity as Player

        if(IngeniaPlayer(player).allowDamage && IngeniaPlayer(damagedPlayer).allowDamage)
            event.isCancelled = false
    }

}