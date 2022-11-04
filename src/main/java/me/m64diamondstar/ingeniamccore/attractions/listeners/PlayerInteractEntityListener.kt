package me.m64diamondstar.ingeniamccore.attractions.listeners

import me.m64diamondstar.ingeniamccore.Main
import me.m64diamondstar.ingeniamccore.attractions.custom.FreeFall
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionManager
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionType
import me.m64diamondstar.ingeniamccore.attractions.utils.SeatRegistry
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import java.util.*

class PlayerInteractEntityListener: Listener {

    @EventHandler
    fun onPlayerInteractAtEntity(event: PlayerInteractAtEntityEvent){
        val entity = event.rightClicked
        val player = event.player

        if(entity.type != EntityType.ARMOR_STAND) return
        if(entity.customName == null) return
        if(!SeatRegistry.getList().contains(entity.uniqueId)) return

        event.isCancelled = true

        if(AttractionType.valueOf(Objects.requireNonNull<String?>(entity.customName).split("-").toTypedArray()[0]) == AttractionType.FREEFALL){
            val freefall = FreeFall(entity.customName!!.split("-")[3], entity.customName!!.split("-")[2])

            if(entity.passengers.size != 0)
                return

            AttractionManager.countdown(freefall)
            entity.addPassenger(player)

        }
    }

}