package me.m64diamondstar.ingeniamccore.entity.body.listeners

import io.papermc.paper.event.player.PlayerTrackEntityEvent
import io.papermc.paper.event.player.PlayerUntrackEntityEvent
import kotlinx.coroutines.Runnable
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.entity.body.NametagEntity
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDismountEvent
import org.bukkit.event.entity.EntityMountEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerToggleSneakEvent

class NametagListener: Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(IngeniaMC.plugin, Runnable {
            NametagEntity.NametagManager.addPlayer(event.player) // Handles spawning of nametag
        }, 1)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(IngeniaMC.plugin, Runnable {
            NametagEntity.NametagManager.removePlayer(event.player) // Handles de-spawning of nametag
        }, 1)
    }

    @EventHandler
    fun onPlayerTrack(event: PlayerTrackEntityEvent){
        if(event.entity !is Player) return
        val player = event.player
        val tracked = event.entity as Player

        if(NametagEntity.NametagManager.getPacketEntity(tracked) == null) return

        NametagEntity.NametagManager.updateEntity(player, tracked)
    }

    @EventHandler
    fun onPlayerUntrack(event: PlayerUntrackEntityEvent){
        if(event.entity !is Player) return
        val player = event.player
        val tracked = event.entity as Player

        if(NametagEntity.NametagManager.getPacketEntity(tracked) == null) return

        NametagEntity.NametagManager.removeEntity(player, tracked)
    }

    @EventHandler
    fun onPlayerEnterVehicle(event: EntityMountEvent){ // Despawn nametag when player enters a vehicle
        if(event.entity !is Player) return
        val player = event.entity as Player

        if(NametagEntity.NametagManager.getPacketEntity(player) == null) return
        NametagEntity.NametagManager.updateMounted(player, true) // Handles de-spawning of nametag

    }

    @EventHandler
    fun onPlayerLeaveVehicle(event: EntityDismountEvent){ // Re-spawn nametag when player leaves a vehicle
        if(event.entity !is Player) return
        val player = event.entity as Player

        if(NametagEntity.NametagManager.getPacketEntity(player) == null) return
        Bukkit.getScheduler().runTaskLaterAsynchronously(IngeniaMC.plugin, Runnable {
            NametagEntity.NametagManager.updateMounted(player, false) // Handles spawning of nametag
        }, 1)
    }

    @EventHandler
    fun toggleSneak(event: PlayerToggleSneakEvent){
        val player = event.player

        if(event.isSneaking)
            NametagEntity.NametagManager.updateSneaking(player, true)
        else
            NametagEntity.NametagManager.updateSneaking(player, false)
    }



}