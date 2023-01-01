package me.m64diamondstar.ingeniamccore.shows.type

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.shows.utils.EffectType
import me.m64diamondstar.ingeniamccore.shows.utils.Show
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import org.bukkit.Bukkit
import org.bukkit.Material

/**
 * Spawns a redstone torch for the given amount of time on a specific location.
 */
class Activator(show: Show, id: Int) : EffectType(show, id) {
    override fun execute() {
        val location = LocationUtils.getLocationFromString(getSection().getString("Location")!!) ?: return
        val duration = if (getSection().get("Duration") != null) getSection().getLong("Duration") else 0
        val material = location.block.type

        location.block.type = Material.REDSTONE_TORCH

        Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, {
            location.block.type = material
        }, duration)
    }

    override fun getType(): Types {
        return Types.ACTIVATOR
    }

    override fun isSync(): Boolean {
        return true
    }

}