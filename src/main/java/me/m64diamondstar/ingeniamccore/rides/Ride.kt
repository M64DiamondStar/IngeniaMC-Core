package me.m64diamondstar.ingeniamccore.rides

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.rides.actions.RideExecution
import me.m64diamondstar.ingeniamccore.rides.actions.RideListener
import me.m64diamondstar.ingeniamccore.rides.events.RideEvent
import org.bukkit.configuration.ConfigurationSection
import java.io.File

class Ride(val id: String) {

    /**
     * Manages, saves and retrieves all loaded rides.
     */
    companion object {
        private val rides = HashMap<String, Ride>()

        /**
         * @return all loaded rides
         */
        fun getRides(): HashMap<String, Ride> {
            return rides
        }

        /**
         * @param id the id of the ride
         * @return the ride with the specified id
         */
        fun getRide(id: String): Ride? {
            return rides[id]
        }

        /**
         * @param id the id of the ride
         * @return if the ride with the specified id exists
         */
        fun exists(id: String): Boolean {
            return rides.containsKey(id)
        }

        /**
         * @param id the id of the ride
         * @param ride the ride to add
         */
        fun addRide(id: String, ride: Ride) {
            if(exists(id)) return
            rides[id] = ride
        }

        /**
         * Clears the current saved rides and loads them from the configurations folder.
         */
        fun loadFromFile(){
            rides.clear()
            val folder = File(IngeniaMC.plugin.dataFolder, "rides/configurations")
            folder.listFiles()?.forEach {
                if(!it.name.contains(".DS_Store"))
                    addRide(it.nameWithoutExtension, Ride(it.name))
            }
        }
    }

    /*
        This marks the start of the rides class
     */

    private val rideConfiguration = RideConfiguration(this)
    private val rideLeaderboard = RideLeaderboard(this)
    private val rideVariables = HashMap<String, RideVariable>()

    var displayName: String
        /**
         * @return the display name of the ride
         */
        get() {
            return rideConfiguration.getConfig().getString("properties.display-name") ?: id
        }

        /**
         * @param value the display name of the ride
         */
        set(value) {
            rideConfiguration.getConfig().set("properties.display-name", value)
            rideConfiguration.save()
        }

    /**
     * Sets a ride variable. Variables are used to store values that can be used in expressions
     * @param variableName the name of the variable
     * @param value the value of the variable
     * @param variableType the type of the variable
     */
    fun setVariable(variableName: String, value: String, variableType: RideVariableType){
        rideVariables[variableName] = RideVariable(value, variableType)
    }

    /**
     * @param variableName the name of the variable
     * @return the variable with the specified name
     */
    fun getVariable(variableName: String): RideVariable?{
        return rideVariables[variableName]
    }

    /**
     * @return all variables
     */
    fun getVariables(): HashMap<String, RideVariable>{
        return rideVariables
    }

    /**
     * Removes a variable
     * @param variableName the name of the variable
     */
    fun removeVariable(variableName: String){
        rideVariables.remove(variableName)
    }

    /**
     * Gets the configuration section containing all the properties for an execution
     * @param id the id of the execution
     * @return the configuration section
     */
    fun getExecutionSection(id: String): ConfigurationSection?{
        return rideConfiguration.getConfig().getConfigurationSection("executions.$id")
    }

    /**
     * Executes a specific ride execution
     * @param id the id of the execution
     * @return whether the execution was successful
     */
    fun execute(id: String): Boolean{
        return RideExecution.getByType(id)?.execute(this, id) == true
    }

    /**
     * Gets the configuration section containing all the properties for a listener
     * @param string the id of the listener
     * @return the configuration section
     */
    fun getListenerSection(string: String): ConfigurationSection?{
        return rideConfiguration.getConfig().getConfigurationSection("listeners.$string")
    }

    /**
     * Gets all listener sections
     * @return all listener sections
     */
    fun getAllListenerSections(): List<ConfigurationSection> {
        return rideConfiguration.getConfig().getConfigurationSection("listeners")?.getKeys(false)?.map {
            rideConfiguration.getConfig().getConfigurationSection("listeners.$it") ?: return emptyList()
        } ?: emptyList()
    }

    /**
     * Execute all listeners with this specific event
     * @param event the ride event which got called
     */
    fun executeListener(event: RideEvent){
        getAllListenerSections().forEach { section ->
            section.getString("type")?.let { type -> RideListener.getByType(type)?.onEventTriggered(this, section.name, event) }
        }
    }


}