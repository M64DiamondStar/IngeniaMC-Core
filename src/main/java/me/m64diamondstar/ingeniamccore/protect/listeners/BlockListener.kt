package me.m64diamondstar.ingeniamccore.protect.listeners

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.ProjectileHitEvent

class BlockListener: Listener {

    @EventHandler
    fun onBlockBrear(event: BlockBreakEvent){
        val player = event.player
        if(player.hasPermission("ingenia.admin") || player.isOp) return
        event.isCancelled = true
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent){
        val player = event.player
        if(player.hasPermission("ingenia.admin") || player.isOp) return
        event.isCancelled = true
    }

    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent){
        if(event.hitBlock == null) return
        if(event.hitBlock!!.type == Material.DECORATED_POT) event.isCancelled = true
    }

}