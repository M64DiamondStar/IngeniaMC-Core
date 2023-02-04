package me.m64diamondstar.ingeniamccore.shows.type

import me.m64diamondstar.ingeniamccore.shows.utils.Effect
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.shows.utils.Show
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * Spawns a redstone torch for the given amount of time on a specific location.
 */
class Activator(show: Show, id: Int) : Effect(show, id) {
    override fun execute(players: List<Player>?) {
        val location = LocationUtils.getLocationFromString(getSection().getString("Location")!!) ?: return
        val duration = if (getSection().get("Duration") != null) getSection().getLong("Duration") else 0

        location.block.type = Material.REDSTONE_TORCH

        Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, {
            location.block.type = Material.AIR
        }, duration)
    }

    override fun getType(): Type {
        return Type.ACTIVATOR
    }

    override fun isSync(): Boolean {
        return true
    }

    override fun getDefaults(): List<Pair<String, Any>> {
        val list = ArrayList<Pair<String, Any>>()
        list.add(Pair("Type", "ACTIVATOR"))
        list.add(Pair("Location", "world, 0, 0, 0"))
        list.add(Pair("Duration", 5))
        list.add(Pair("Delay", 0))
        return list
    }

}