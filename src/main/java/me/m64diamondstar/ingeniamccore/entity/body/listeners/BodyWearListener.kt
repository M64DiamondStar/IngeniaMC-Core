package me.m64diamondstar.ingeniamccore.entity.body.listeners

import io.papermc.paper.event.player.PlayerTrackEntityEvent
import io.papermc.paper.event.player.PlayerUntrackEntityEvent
import kotlinx.coroutines.Runnable
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.entity.body.BodyWearEntity
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDismountEvent
import org.bukkit.event.entity.EntityMountEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class BodyWearListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(IngeniaMC.plugin, Runnable {
            BodyWearEntity.BodyWearManager.addPlayer(event.player) // Handles spawning of body wear
        }, 1)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(IngeniaMC.plugin, Runnable {
            BodyWearEntity.BodyWearManager.removePlayer(event.player) // Handles de-spawning of body wear
        }, 1)
    }

    @EventHandler
    fun onPlayerTrack(event: PlayerTrackEntityEvent){
        if(event.entity !is Player) return
        val player = event.player
        val tracked = event.entity as Player

        if(BodyWearEntity.BodyWearManager.getPacketEntity(tracked) == null) return

        BodyWearEntity.BodyWearManager.updateEntity(player, tracked)
    }

    @EventHandler
    fun onPlayerUntrack(event: PlayerUntrackEntityEvent){
        if(event.entity !is Player) return
        val player = event.player
        val tracked = event.entity as Player

        if(BodyWearEntity.BodyWearManager.getPacketEntity(tracked) == null) return

        BodyWearEntity.BodyWearManager.removeEntity(player, tracked)
    }

    @EventHandler
    fun onPlayerEnterVehicle(event: EntityMountEvent){ // Despawn body wear when player enters a vehicle
        if(event.entity !is Player) return
        val player = event.entity as Player

        player.sendMessage("Entered vehicle")

        if(BodyWearEntity.BodyWearManager.getPacketEntity(player) == null) return
        BodyWearEntity.BodyWearManager.updateMounted(player, true) // Handles de-spawning of body wear

    }

    @EventHandler
    fun onPlayerLeaveVehicle(event: EntityDismountEvent){ // Re-spawn body wear when player leaves a vehicle
        if(event.entity !is Player) return
        val player = event.entity as Player

        player.sendMessage("Exited vehicle")

        if(BodyWearEntity.BodyWearManager.getPacketEntity(player) == null) return
        Bukkit.getScheduler().runTaskLaterAsynchronously(IngeniaMC.plugin, Runnable {
            BodyWearEntity.BodyWearManager.updateMounted(player, false) // Handles spawning of body wear
        }, 1)
    }

    /*@EventHandler
    fun toggleSneak(event: PlayerToggleSneakEvent){

    }*/
}
