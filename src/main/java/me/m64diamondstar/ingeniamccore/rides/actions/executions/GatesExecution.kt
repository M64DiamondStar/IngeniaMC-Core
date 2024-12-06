package me.m64diamondstar.ingeniamccore.rides.actions.executions

import me.m64diamondstar.ingeniamccore.rides.Ride
import me.m64diamondstar.ingeniamccore.rides.actions.RideExecution
import me.m64diamondstar.ingeniamccore.rides.actions.utils.RideParameter
import me.m64diamondstar.ingeniamccore.utils.GateDirection
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import org.bukkit.block.BlockFace
import org.bukkit.block.data.type.Gate

/**
 * An executive which manages gates, you can open or close them and change their facing.
 */
class GatesExecution: RideExecution {
    override fun getType(): String {
        return "gates"
    }

    override fun execute(ride: Ride, id: String): Boolean {
        val section = ride.getExecutionSection(id) ?: return false
        val open = if(section.get("open") != null) section.getBoolean("open") else return false
        val direction = if(section.get("direction") != null) BlockFace.valueOf(section.getString("direction")!!.uppercase()) else return false
        val locations = if(section.get("gates") != null) section.getStringList("gates").map { LocationUtils.getLocationFromString(it) } else return false

        locations.forEach {
            if(it == null) return@forEach
            if(it.block.blockData !is Gate) return@forEach
            val gate = it.block.blockData as Gate
            gate.isOpen = open
            gate.facing = direction
        }

        return true
    }

    override fun getDefaults(): List<RideParameter> {
        val list = ArrayList<RideParameter>()
        list.add(RideParameter("open", false, { it.toBoolean() }) { it.toBooleanStrictOrNull() != null })
        list.add(RideParameter("direction", "NORTH", { it.uppercase() }) { GateDirection.entries.map { it.name }.contains(it.uppercase()) })
        list.add(RideParameter("gates", listOf("ThemePark, 0, 0, 0", "ThemePark, 1, 0, 0"), { it.toBoolean() }) { it.toBooleanStrictOrNull() != null })
        return list
    }
}