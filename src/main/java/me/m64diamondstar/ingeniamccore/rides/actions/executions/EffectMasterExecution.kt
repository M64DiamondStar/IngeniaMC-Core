package me.m64diamondstar.ingeniamccore.rides.actions.executions

import me.m64diamondstar.effectmaster.shows.EffectShow
import me.m64diamondstar.ingeniamccore.rides.Ride
import me.m64diamondstar.ingeniamccore.rides.actions.RideExecution
import me.m64diamondstar.ingeniamccore.rides.actions.utils.RideParameter
import java.util.ArrayList

/**
 * Plays an EffectMaster show
 */
class EffectMasterExecution: RideExecution {
    override fun getType(): String {
        return "effectmaster_show"
    }

    override fun execute(ride: Ride, id: String): Boolean {
        val section = ride.getExecutionSection(id) ?: return false
        val category = if(section.getString("category") != null) section.getString("category")!! else return false
        val name = if(section.getString("name") != null) section.getString("name")!! else return false
        EffectShow(category, name).play(null) // Play a show for every player on the server
        return true
    }

    override fun getDefaults(): List<RideParameter> {
        val list = ArrayList<RideParameter>()
        list.add(RideParameter("category", "show_category", { it }) { true })
        list.add(RideParameter("name", "show_name", { it }) { true }) // Always allow new power channels, it'll create them if they don't exist yet.
        return list
    }
}