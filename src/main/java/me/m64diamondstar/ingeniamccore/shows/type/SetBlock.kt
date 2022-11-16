package me.m64diamondstar.ingeniamccore.shows.type

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.shows.utils.EffectType
import me.m64diamondstar.ingeniamccore.shows.utils.Show
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import org.bukkit.Bukkit
import org.bukkit.Material

class SetBlock(show: Show, id: Int) : EffectType(show, id) {

    override fun execute() {
        val location = LocationUtils.getLocationFromString(getSection().getString("Location")!!) ?: return
        val material =
            if (getSection().get("Block") != null) Material.valueOf(getSection().getString("Block")!!) else Material.STONE
        val duration = if (getSection().get("Duration") != null) getSection().getLong("Duration") else 0
        val normalBlock = location.block.blockData

        for(player in Bukkit.getOnlinePlayers())
            player.sendBlockChange(location, material.createBlockData())

        Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, {
            for(player in Bukkit.getOnlinePlayers())
                player.sendBlockChange(location, normalBlock)
        }, duration)

    }

    override fun getType(): Types {
        return Types.SET_BLOCK
    }

    override fun isSync(): Boolean {
        return true
    }
}