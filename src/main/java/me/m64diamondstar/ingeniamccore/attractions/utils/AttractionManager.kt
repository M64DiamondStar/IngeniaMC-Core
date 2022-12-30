package me.m64diamondstar.ingeniamccore.attractions.utils

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.entities.EntityUtils
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

object AttractionManager {

    fun countdown(attraction: CustomAttraction) {

        if(attraction.hasPassengers())
            return

        object: BukkitRunnable(){
            var c = attraction.getAttraction().getCountdownTime()
            override fun run() {

                if(c == 0){
                    attraction.dispatch()
                    this.cancel()
                    return
                }

                var hasPassenger = false
                attraction.getSeats().forEach {
                    if(it.passengers.size == 1){
                        attraction.getAttraction().getCountdownType().sendActionBarMessage(player = it.passengers[0] as Player, c)
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

    fun setLocked(attraction: CustomAttraction, locked: Boolean){
        attraction.getSeats().forEach { EntityUtils.setLocked(it, locked) }
    }

}