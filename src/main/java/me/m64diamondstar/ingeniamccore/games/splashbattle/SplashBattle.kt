package me.m64diamondstar.ingeniamccore.games.splashbattle

import me.m64diamondstar.ingeniamccore.data.LoadedConfiguration
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import java.util.*
import kotlin.math.sqrt
import kotlin.random.Random

class SplashBattle(val name: String): LoadedConfiguration("games/splashbattle", name, false, true) {

    fun create(world: World){
        this.getConfig().set("Display-Name", name)
        this.getConfig().set("World", world.name)
        this.getConfig().set("Join-Sign", "${world.name}, 0, 0, 0")

        val list = ArrayList<String>()

        list.add("Water Battle Configuration of $name")

        this.getConfig().options().setHeader(list)
        this.reloadConfig()
    }

    var world: World
        get() {
            if(this.getConfig().getString("World") == null){
                this.getConfig().set("World", "world")
                this.reloadConfig()
            }

            return Bukkit.getWorld(this.getConfig().getString("World")!!)!!
        }

        set(value) {
            this.getConfig().set("World", value.name)
            this.reloadConfig()
        }

    var joinSignLocation: Location
        get() {
            return LocationUtils.getLocationFromString(this.getConfig().getString("Join-Sign")!!)!!
        }

        set(value) {
            this.getConfig().set("Join-Sign", LocationUtils.getStringFromLocation(value))
            reloadConfig()
        }

    var displayName: String
        get() {
            return this.getConfig().getString("Display-Name")!!
        }

        set(value) {
            this.getConfig().set("Display-Name", value)
            this.reloadConfig()
        }

    fun addSpawnPoint(location: Location) {
        val list = this.getConfig().getStringList("Arena.Spawn-Points")
        list.add(LocationUtils.getStringFromLocation(location))
        this.getConfig().set("Arena.Spawn-Points", list)
        reloadConfig()
    }

    fun getRandomSpawnPoint(): Location{
        val list = this.getConfig().getStringList("Arena.Spawn-Points")
        return LocationUtils.getLocationFromString(list[Random.nextInt(list.size)])!!
    }

    fun getRandomSpawnPoint(location: Location): Location{
        val stringList = this.getConfig().getStringList("Arena.Spawn-Points")
        val locationList = ArrayList<Location>()
        stringList.forEach { locationList.add(LocationUtils.getLocationFromString(it)!!) }
        if(locationList.size > 3)
            locationList.removeAll(LocationUtil.getClosestLocations(locationList, location).toSet())

        return locationList[Random.nextInt(locationList.size)]
    }

    private object LocationUtil {
        fun getClosestLocations(locationList: List<Location>, specificLocation: Location): List<Location> {
            // Create a priority queue to store the closest locations based on distance
            val closestLocations = PriorityQueue(Comparator.comparingDouble(DistanceWrapper::distance))

            // Calculate the distance between each location and the specific location and add them to the queue
            for (location in locationList) {
                val distance = getDistance(location, specificLocation)
                closestLocations.add(DistanceWrapper(location, distance))
            }

            // Extract the 3 closest locations from the queue
            val result = mutableListOf<Location>()
            val count = 3.coerceAtMost(closestLocations.size)
            for (i in 0 until count) {
                result.add(closestLocations.poll().location)
            }

            return result
        }

        private fun getDistance(loc1: Location, loc2: Location): Double {
            // Calculate Euclidean distance between two locations
            val dx = loc1.x - loc2.x
            val dy = loc1.y - loc2.y
            val dz = loc1.z - loc2.z
            return sqrt(dx * dx + dy * dy + dz * dz)
        }

        // Helper class to wrap a location with its distance from the specific location
        private data class DistanceWrapper(val location: Location, val distance: Double)
    }

    fun getLeaderboard(): SplashBattleLeaderboard{
        return SplashBattleLeaderboard(this)
    }

}