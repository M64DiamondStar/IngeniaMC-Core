package me.m64diamondstar.ingeniamccore.attractions.utils

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.MessageLocation
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

object AttractionManager {

    fun spawnAttractions() {}

    fun countdown(attraction: CustomAttraction) {

        if(attraction.hasPassengers())
            return

        object: BukkitRunnable(){
            var c = 15
            override fun run() {

                if(c == 0){
                    attraction.execute()
                    this.cancel()
                    return
                }

                var hasPassenger = false
                attraction.getSeats().forEach {
                    if(it.passengers.size == 1){
                        IngeniaPlayer(it.passengers[0] as Player).sendMessage(Messages.rideCountdown(c), MessageLocation.HOTBAR)
                        hasPassenger = true
                    }
                }

                if(!hasPassenger){
                    this.cancel()
                    return
                }


                c -= 1
            }
        }.runTaskTimer(IngeniaMC.plugin, 5L, 20L)

    }

}