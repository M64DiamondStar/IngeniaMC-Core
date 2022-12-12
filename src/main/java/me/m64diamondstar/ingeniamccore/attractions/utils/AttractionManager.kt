package me.m64diamondstar.ingeniamccore.attractions.utils

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.scheduler.BukkitRunnable

object AttractionManager {

    fun countdown(attraction: CustomAttraction) {

        if(attraction.hasPassengers())
            return

        object: BukkitRunnable(){
            var c = 15
            override fun run() {

                if(c == 0){
                    attraction.dispatch()
                    this.cancel()
                    return
                }

                var hasPassenger = false
                attraction.getSeats().forEach {
                    if(it.passengers.size == 1){
                        (it.passengers[0] as Audience).sendActionBar(
                            Component.text(Messages.rideCountdown(c)).color(
                                TextColor
                                    .fromHexString(MessageType.PLAYER_UPDATE)))
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