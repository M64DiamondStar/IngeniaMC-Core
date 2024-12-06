package me.m64diamondstar.ingeniamccore.rides.actions

import me.m64diamondstar.ingeniamccore.rides.Ride
import org.bukkit.event.Event
import org.bukkit.event.Listener

interface RideListener : Listener {

    companion object {

        private val listeners = HashMap<String, RideListener>()

        /**
         * Registers a new executive.
         * @param listener the new executive
         */
        fun register(listener: RideListener) {
            listeners[listener.eventType.name.lowercase()] = listener
        }

        /**
         * Returns the executive with the specified type.
         * @param type the type of the executive
         * @return the executive with the specified type
         */
        fun getByType(type: String): RideListener? {
            listeners[type.lowercase()]?.let { return it }
            return null
        }

        /**
         * Gets all registered executives.
         * @return all registered executives
         */
        fun getListeners(): Map<String, RideListener> {
            return listeners.toMap()
        }

    }

    /**
     * The event type that this listener will respond to
     */
    val eventType: Class<out Event>

    /**
     * The identifier of this listener
     */
    fun getType(): String

    /**
     * Executes when the event is triggered, checking if any conditions are met to
     * perform the specified `RideExecutive` actions.
     *
     * @param ride the ride that triggered the event
     * @param id the id used for this listener
     * @param event the triggered event
     */
    fun onEventTriggered(ride: Ride, id: String, event: Event)
}
