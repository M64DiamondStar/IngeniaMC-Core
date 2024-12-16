package me.m64diamondstar.ingeniamccore.rides.actions

import me.m64diamondstar.ingeniamccore.rides.Ride
import me.m64diamondstar.ingeniamccore.rides.actions.utils.RideParameter

interface RideExecution {

    companion object {

        private val executives = HashMap<String, RideExecution>()

        /**
         * Registers a new executive.
         * @param executive the new executive
         */
        fun register(executive: RideExecution) {
            executives[executive.getType().lowercase()] = executive
        }

        /**
         * Returns the executive with the specified type.
         * @param type the type of the executive
         * @return the executive with the specified type
         */
        fun getByType(type: String): RideExecution? {
            executives[type.lowercase()]?.let { return it }
            return null
        }

    }

    /**
     * The type of the executive
     */
    fun getType(): String

    /**
     * Executes this executive for the specific ride.
     * @param ride the ride to execute this action on
     * @param id the id used for this executive
     * @return whether the action was successful
     */
    fun execute(ride: Ride, id: String): Boolean

    /**
     * @return the default settings of this executive
     */
    fun getDefaults(): List<RideParameter>

}