package me.m64diamondstar.ingeniamccore.rides.events

import me.m64diamondstar.ingeniamccore.rides.Ride
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

abstract class RideEvent(var ride: Ride): Event() {

    companion object{
        @JvmStatic
        val HANDLERS_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS_LIST
        }
    }

    override fun getHandlers(): HandlerList {
        return HANDLERS_LIST
    }
}