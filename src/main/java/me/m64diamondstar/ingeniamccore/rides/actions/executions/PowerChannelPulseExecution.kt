package me.m64diamondstar.ingeniamccore.rides.actions.executions

import com.bergerkiller.bukkit.coasters.TCCoasters
import me.m64diamondstar.ingeniamccore.rides.Ride
import me.m64diamondstar.ingeniamccore.rides.actions.RideExecution
import me.m64diamondstar.ingeniamccore.rides.actions.utils.RideParameter
import org.bukkit.Bukkit

class PowerChannelPulseExecution: RideExecution {
    override fun getType(): String {
        return "power_channel_pulse"
    }

    override fun execute(ride: Ride, id: String): Boolean {
        val tcCoastersPlugin = Bukkit.getPluginManager().getPlugin("TCCoasters")
        if(tcCoastersPlugin?.isEnabled != true) return false // If the plugin is not enabled, or doesn't exist, fail the execution
        val section = ride.getExecutionSection(id) ?: return false
        val world = if(section.getString("world") != null) Bukkit.getWorld(section.getString("world")!!) else return false
        val channelName = if(section.getString("channel") != null) section.getString("channel")!! else return false
        val pulse = if(section.get("pulse") != null) section.getInt("pulse") else return false

        if(world == null) return false // The world doesn't exist
        if(pulse < 1) return false // The pulse time cannot be less than 1 tick

        val namedPowerChannelRegistry = (tcCoastersPlugin as TCCoasters).getCoasterWorld(world).namedPowerChannels
        if(namedPowerChannelRegistry.findIfExists(channelName) == null) return false // The channel doesn't exist

        val powerChannel = namedPowerChannelRegistry.findIfExists(channelName)
        powerChannel.pulsePowered(powerChannel.isPowered.not(), pulse) // Pulse the power channel for its reverse state
        return true
    }

    override fun getDefaults(): List<RideParameter> {
        val list = ArrayList<RideParameter>()
        list.add(RideParameter("world", false, { it }) { Bukkit.getWorld(it) != null })
        list.add(RideParameter("channel", "default", { it }) { true }) // Always allow new power channels, it'll create them if they don't exist yet.
        list.add(RideParameter("pulse", 20, { it.toInt() }) { it.toIntOrNull() != null && it.toInt() > 0 }) // The pulse time in ticks
        return list
    }
}