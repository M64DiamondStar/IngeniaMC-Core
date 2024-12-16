package me.m64diamondstar.ingeniamccore.rides

import com.bergerkiller.bukkit.tc.events.MemberEvent
import com.bergerkiller.bukkit.tc.events.seat.MemberBeforeSeatEnterEvent
import com.bergerkiller.bukkit.tc.events.seat.MemberBeforeSeatExitEvent
import com.bergerkiller.bukkit.tc.events.seat.MemberSeatEnterEvent
import com.bergerkiller.bukkit.tc.events.seat.MemberSeatExitEvent
import me.m64diamondstar.ingeniamccore.rides.events.RideCartSeatBeforeEnterEvent
import me.m64diamondstar.ingeniamccore.rides.events.RideCartSeatBeforeExitEvent
import me.m64diamondstar.ingeniamccore.rides.events.RideEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class RideListener: Listener {

    @EventHandler
    fun onEvent(event: RideEvent) {
        Ride.getRides().forEach {
            it.value.executeListener(event)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerInteractEntity(event: MemberEvent){
        if(event !is MemberBeforeSeatEnterEvent && event !is MemberBeforeSeatExitEvent
            && event !is MemberSeatEnterEvent && event !is MemberSeatExitEvent) return

        val member = event.member
        member.signTracker.activeTrackedSigns.forEach {
            if(it.sign.getLine(1).contains("ride detector")){
                val ride = Ride.getRide(it.sign.getLine(2)) ?: return@forEach
                val channel = it.sign.getLine(3)

                // Check which kind of event is fired
                when(event){

                    // Before a player enters a seat
                    is MemberBeforeSeatEnterEvent -> {
                        val event = RideCartSeatBeforeEnterEvent(
                            ride,
                            channel,
                            member.group.map { member -> member.entity.playerPassengers }.flatten()
                        )
                        Bukkit.getPluginManager().callEvent(event)
                    }

                    // Before a player exits a seat
                    is MemberBeforeSeatExitEvent -> {
                        val event = RideCartSeatBeforeExitEvent(
                            ride,
                            channel,
                            member.group.map { member -> member.entity.playerPassengers }.flatten()
                        )
                        Bukkit.getPluginManager().callEvent(event)
                    }

                    // After a player enters a seat
                    is MemberSeatEnterEvent -> {
                        val event = RideCartSeatBeforeEnterEvent(
                            ride,
                            channel,
                            member.group.map { member -> member.entity.playerPassengers }.flatten()
                        )
                        Bukkit.getPluginManager().callEvent(event)
                    }

                    // After a player exits a seat
                    is MemberSeatExitEvent -> {
                        val event = RideCartSeatBeforeExitEvent(
                            ride,
                            channel,
                            member.group.map { member -> member.entity.playerPassengers }.flatten()
                        )
                        Bukkit.getPluginManager().callEvent(event)
                    }

                }
            }
        }
    }

}