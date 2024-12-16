package me.m64diamondstar.ingeniamccore.rides.actions.executions

import com.bergerkiller.bukkit.coasters.TCCoasters
import me.m64diamondstar.ingeniamccore.rides.Ride
import me.m64diamondstar.ingeniamccore.rides.actions.RideExecution
import me.m64diamondstar.ingeniamccore.rides.actions.utils.RideParameter
import org.bukkit.Bukkit

/**
 * An executive which toggles a TCCoaster Power Channel on or off
 */
class PowerChannelToggleExecution: RideExecution {
    override fun getType(): String {
        return "power_channel_toggle"
    }

    override fun execute(ride: Ride, id: String): Boolean {
        val tcCoastersPlugin = Bukkit.getPluginManager().getPlugin("TCCoasters")
        if(tcCoastersPlugin?.isEnabled != true) return false // If the plugin is not enabled, or doesn't exist, fail the execution
        val section = ride.getExecutionSection(id) ?: return false
        val world = if(section.getString("world") != null) Bukkit.getWorld(section.getString("world")!!) else return false
        val channelName = if(section.getString("channel") != null) section.getString("channel")!! else return false
        val powered = if(section.get("powered") != null) section.getBoolean("powered") else return false

        if(world == null) return false // The world doesn't exist

        val namedPowerChannelRegistry = (tcCoastersPlugin as TCCoasters).getCoasterWorld(world).namedPowerChannels
        if(namedPowerChannelRegistry.findIfExists(channelName) == null) return false // The channel doesn't exist

        val powerChannel = namedPowerChannelRegistry.findIfExists(channelName)
        powerChannel.isPowered = powered // Set the power channel to the desired state
        return true
    }

    override fun getDefaults(): List<RideParameter> {
        val list = ArrayList<RideParameter>()
        list.add(RideParameter("world", false, { it }) { Bukkit.getWorld(it) != null })
        list.add(RideParameter("channel", "default", { it }) { true }) // Always allow new power channels, it'll create them if they don't exist yet.
        list.add(RideParameter("powered", false, { it.toBoolean() }) { it.toBooleanStrictOrNull() != null }) // The power of the channel, true if it should turn on, false if it should turn off
        return list
    }
}