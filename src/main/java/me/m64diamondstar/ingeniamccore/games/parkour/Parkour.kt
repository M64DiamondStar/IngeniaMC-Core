package me.m64diamondstar.ingeniamccore.games.parkour

import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

class Parkour(val category: String, val name: String): DataConfiguration("games/parkour/$category", name) {

    fun create(world: World){
        this.getConfig().set("Display-Name", name)
        this.getConfig().set("World", world.name)

        val list = ArrayList<String>()

        list.add("Parkour Configuration of $name")

        this.getConfig().options().setHeader(list)
        this.save()
    }

    val world: World
        get() {
            return Bukkit.getWorld(this.getConfig().getString("World")!!)!!
        }

    var displayName: String
        get() {
            return this.getConfig().getString("Display-Name")!!
        }

        set(value) {
            this.getConfig().set("Display-Name", value)
            this.save()
        }

    var startLocation: Location?
        get(){
            return LocationUtils.getLocationFromString("${world.name}, ${this.getConfig().getString("Start-Location")}")
        }

        set(value) {
            if (value != null) {
                this.getConfig().set("Start-Location", "${value.x}, ${value.y}, ${value.z}")
                this.save()
            }
        }

    var endLocation: Location?
        get(){
            return LocationUtils.getLocationFromString("${world.name}, ${this.getConfig().getString("End-Location")}")
        }

        set(value) {
            if (value != null) {
                this.getConfig().set("End-Location", "${value.x}, ${value.y}, ${value.z}")
                this.save()
            }
        }

    var startRadius: Double
        get() = this.getConfig().getDouble("Start-Distance")
        set(value) {
            this.getConfig().set("Start-Distance", value)
            this.save()
        }

    var endRadius: Double
        get() = this.getConfig().getDouble("End-Distance")
        set(value) {
            this.getConfig().set("End-Distance", value)
            this.save()
        }

    fun isInstalled(): Boolean{
        if(endLocation == null)
            return false
        if(startLocation == null)
            return false
        if(endRadius == 0.0)
            return false
        if(startRadius == 0.0)
            return false
        return true
    }

    fun getLeaderboard(): ParkourLeaderboard {
        return ParkourLeaderboard(this)
    }
}