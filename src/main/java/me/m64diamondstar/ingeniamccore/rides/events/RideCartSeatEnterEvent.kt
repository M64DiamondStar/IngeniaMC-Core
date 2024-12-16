package me.m64diamondstar.ingeniamccore.rides.events

import me.m64diamondstar.ingeniamccore.rides.Ride
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList

class RideCartSeatEnterEvent(ride: Ride, val channel: String, val passengers: List<Player>) : RideEvent(ride) {

    private var isCancelled = false

    override fun getHandlers(): HandlerList {
        return HANDLERS_LIST
    }

    companion object {
        @JvmStatic
        val HANDLERS_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS_LIST
        }
    }
}