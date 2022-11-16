package me.m64diamondstar.ingeniamccore.shows.type

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.shows.utils.EffectType
import me.m64diamondstar.ingeniamccore.shows.utils.Show
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.checkerframework.checker.units.qual.min
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min


class FillBlock(show: Show, id: Int) : EffectType(show, id) {

    override fun execute() {
        val fromLocation = LocationUtils.getLocationFromString(getSection().getString("FromLocation")!!) ?: return
        val toLocation = LocationUtils.getLocationFromString(getSection().getString("ToLocation")!!) ?: return
        val material =
            if (getSection().get("Block") != null) Material.valueOf(getSection().getString("Block")!!) else Material.STONE
        val duration = if (getSection().get("Duration") != null) getSection().getLong("Duration") else 0

        val normalMap = HashMap<Location, BlockData>()

        for (x in fromLocation.blockX.coerceAtLeast(toLocation.blockX) downTo toLocation.blockX.coerceAtMost(fromLocation.blockX)) {
            for (y in fromLocation.blockY.coerceAtLeast(toLocation.blockY) downTo toLocation.blockY.coerceAtMost(fromLocation.blockY)) {
                for (z in fromLocation.blockZ.coerceAtLeast(toLocation.blockZ) downTo toLocation.blockZ.coerceAtMost(fromLocation.blockZ)) {
                    val location = Location(fromLocation.world, x.toDouble(), y.toDouble(), z.toDouble())
                    for(player in Bukkit.getOnlinePlayers())
                        player.sendBlockChange(location, material.createBlockData())
                    normalMap[location] = location.block.blockData
                }
            }
        }

        /*for (x in ceil(fromLocation.x.coerceAtLeast(toLocation.x)).toInt()..floor(min(toLocation.x, fromLocation.x)).toInt() step 1) {
            for (z in ceil(fromLocation.z.coerceAtLeast(toLocation.z)).toInt()..floor(min(toLocation.z, fromLocation.z)).toInt() step 1) {
                for (y in ceil(fromLocation.y.coerceAtLeast(toLocation.y)).toInt()..floor(max(toLocation.y, fromLocation.z)).toInt() step 1) {
                    val location = Location(fromLocation.world, x.toDouble(), y.toDouble(), z.toDouble())
                    for(player in Bukkit.getOnlinePlayers())
                        player.sendBlockChange(location, material.createBlockData())
                    normalMap[location] = location.block.blockData
                }
            }
        }*/

        Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, {
            for(player in Bukkit.getOnlinePlayers())
                for(loc in normalMap.keys)
                    player.sendBlockChange(loc, normalMap[loc]!!)
        }, duration)
    }

    override fun getType(): Types {
        return Types.FILL_BLOCK
    }

    override fun isSync(): Boolean {
        return true
    }
}