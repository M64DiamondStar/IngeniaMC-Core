package me.m64diamondstar.ingeniamccore.attractions.listeners

import com.bergerkiller.bukkit.tc.controller.MinecartGroup
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore
import me.m64diamondstar.ingeniamccore.Main
import me.m64diamondstar.ingeniamccore.attractions.custom.Coaster
import me.m64diamondstar.ingeniamccore.attractions.custom.FreeFall
import me.m64diamondstar.ingeniamccore.attractions.utils.Attraction
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionManager
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionType
import me.m64diamondstar.ingeniamccore.attractions.utils.SeatRegistry
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.MessageLocation
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.collections.ArrayList

class PlayerInteractEntityListener: Listener {

    private val usedCoaster = ArrayList<MinecartGroup>() //So countdown doesn't start again when second player enters

    /**
     * FreeFall listener
     */
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

    /**
     * Coaster listener
     */
    @EventHandler
    fun onPlayerInteractCoasterEvent(event: PlayerInteractEntityEvent){
        val entity = event.rightClicked

        if(MinecartMemberStore.getFromEntity(entity) == null) return

        val member = MinecartMemberStore.getFromEntity(entity)

        if(usedCoaster.contains(member.group)) return

        for(block in member.group.signTracker.activeTrackedSigns){
            if(!block.sign.sign.getLine(1).contains("attraction")) return
            if(block.sign.sign.getLine(1).split(" ")[1].toInt() != 1) return

            usedCoaster.add(member.group)

            val attraction = Attraction(block.sign.sign.getLine(2), block.sign.sign.getLine(3))
            if(attraction.getType() == AttractionType.PARKTRAIN) return

            object: BukkitRunnable(){

                var c = 15

                override fun run() {
                    if(!member.group.hasPassenger()){
                        usedCoaster.remove(member.group)
                        this.cancel()
                        return
                    }

                    if(c == 0){
                        member.group.properties.playersEnter = false
                        member.group.properties.playersExit = false

                        if(attraction.getType() == AttractionType.COASTER){
                            val coaster = Coaster(attraction.getCategory(), attraction.getName())
                            coaster.dispatch()
                        }

                        usedCoaster.remove(member.group)
                        attraction.closeGates()

                        this.cancel()
                        return
                    }

                    if(c != 1)
                        for(members in member.group)
                            members.entity.playerPassengers.forEach { IngeniaPlayer(it).
                            sendMessage("&6This coaster will dispatch in $c seconds.", MessageLocation.HOTBAR) }
                    else
                        for(members in member.group)
                            members.entity.playerPassengers.forEach { IngeniaPlayer(it).
                            sendMessage("&6This coaster will dispatch in 1 second.", MessageLocation.HOTBAR) }
                    c--
                }
            }.runTaskTimer(Main.plugin, 0L, 20L)
        }

    }


}