package me.m64diamondstar.ingeniamccore.games.parkour

import me.m64diamondstar.ingeniamccore.data.Configuration
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

class Parkour(category: String, val name: String): Configuration("games/parkour/$category", name, false, true) {

    fun createParkour(world: World){
        this.getConfig().set("Display-Name", name)
        this.getConfig().set("World", world.name)

        val list = ArrayList<String>()

        list.add("An area is used so that the player can locate himself, and to play the correct music.")
        list.add("You can change the Display-Name to whatever you like, but NO COLOR CODES!")
        list.add("If two or more areas are overlapping, the area with the highest Weight will be selected.")

        this.getConfig().options().setHeader(list)
        this.reloadConfig()
    }

    private val world: World
        get() {
            return Bukkit.getWorld(this.getConfig().getString("World")!!)!!
        }

    var displayName: String
        get() {
            return this.getConfig().getString("Display-Name")!!
        }

        set(value) {
            this.getConfig().set("Display-Name", value)
            this.reloadConfig()
        }

    var startLocation: Location?
        get(){
            return LocationUtils.getLocationFromString("${world.name}, ${this.getConfig().getString("Start-Location")}")
        }

        set(value) {
            if (value != null) {
                this.getConfig().set("Start-Location", "${value.x}, ${value.y}, ${value.z}")
                this.reloadConfig()
            }
        }

    var endLocation: Location?
        get(){
            return LocationUtils.getLocationFromString("${world.name}, ${this.getConfig().getString("End-Location")}")
        }

        set(value) {
            if (value != null) {
                this.getConfig().set("End-Location", "${value.x}, ${value.y}, ${value.z}")
                this.reloadConfig()
            }
        }

    var startRadius: Double
        get() = this.getConfig().getDouble("Start-Distance")
        set(value) {
            this.getConfig().set("Start-Distance", value)
            this.reloadConfig()
        }

    var endRadius: Double
        get() = this.getConfig().getDouble("End-Distance")
        set(value) {
            this.getConfig().set("End-Distance", value)
            this.reloadConfig()
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






}