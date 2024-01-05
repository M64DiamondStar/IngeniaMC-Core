package me.m64diamondstar.ingeniamccore.warps

import me.m64diamondstar.ingeniamccore.IngeniaMC
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object WarpUtils {

    fun getNearestLocation(player: Player): Location{
        val warpManager = WarpManager()
        val locations = warpManager.getAllLocations()
        val playerLocation = player.location
        var nearestLocation = locations[0]

        locations.forEach {
            if(playerLocation.distanceSquared(it) < playerLocation.distanceSquared(nearestLocation))
                nearestLocation = it
        }

        return nearestLocation
    }

    fun getIDFromItem(item: ItemStack): String?{
        val id = item.itemMeta?.persistentDataContainer?.get(NamespacedKey(IngeniaMC.plugin, "warp-id"), PersistentDataType.STRING) ?: return null
        val warpManager = WarpManager()
        return if(warpManager.existsWarp(id)) id else null
    }

}