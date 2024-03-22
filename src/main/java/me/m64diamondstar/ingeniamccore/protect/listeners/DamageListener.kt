package me.m64diamondstar.ingeniamccore.protect.listeners

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause

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

    @EventHandler
    fun onDamage(event: EntityDamageEvent){
        if((event.cause == DamageCause.SUFFOCATION
            || event.cause == DamageCause.FALL
            || event.cause == DamageCause.FLY_INTO_WALL
            || event.cause == DamageCause.DRYOUT
            || event.cause == DamageCause.DROWNING
            || event.cause == DamageCause.CONTACT) && event.entity is Player) {
            event.isCancelled = true
            return
        }
    }

}