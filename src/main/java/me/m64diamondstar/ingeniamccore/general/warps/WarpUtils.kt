package me.m64diamondstar.ingeniamccore.general.warps

import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import org.bukkit.Location
import org.bukkit.entity.Player

object WarpUtils {

    private val list = ArrayList<Location>()

    fun reloadWarpList(){
        list.clear()
        AttractionUtils.getAllAttractions().forEach {
            if(it.getWarpLocation() != null)
                list.add(it.getWarpLocation()!!)
        }
    }

    fun getNearestLocation(player: Player): Location{
        var nearestLocation = list[0]
        val playerLocation = player.location

        for(index in 1 until list.size){
            if(playerLocation.distanceSquared(list[index]) < playerLocation.distanceSquared(nearestLocation))
                nearestLocation = list[index]
        }

        return nearestLocation
    }



}