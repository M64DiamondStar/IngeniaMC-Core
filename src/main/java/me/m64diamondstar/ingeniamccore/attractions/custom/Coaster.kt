package me.m64diamondstar.ingeniamccore.attractions.custom

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.attractions.utils.Attraction
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material

class Coaster(category: String, name: String): Attraction(category, name) {

    fun setRowOccupied(row: Int, occupied: Boolean){
        getConfig().set("Data.Station.Rows.$row.Occupied", occupied)
        reloadConfig()
    }

    fun isRowOccupied(row: Int): Boolean{
        if(getConfig().get("Data.Station.Rows.$row.Occupied") == null)
            return false
        return getConfig().getBoolean("Data.Station.Rows.$row.Occupied")
    }

    fun existsRow(row: Int): Boolean{
        if(getConfig().get("Data.Station.Rows.$row") == null)
            return false
        return true
    }

    fun getRows(): List<Int>{
        if(getConfig().getConfigurationSection("Data.Station.Rows") == null)
            return emptyList()

        val list: ArrayList<Int> = ArrayList()
        getConfig().getConfigurationSection("Data.Station.Rows")?.getKeys(false)?.forEach { list.add(it.toInt()) }
        return list
    }

    /**
     * Sets the station sign of a specific row in this attraction
     */
    fun setRowStation(row: Int, location: Location){
        this.getConfig().set("Data.Station.Rows.$row.StationSign", "${location.x}, ${location.y}, ${location.z}")
        this.reloadConfig()
    }

    /**
     * Get the location of the station sign of a specific row
     * @return the location where a redstone signal should be given
     */
    private fun getRowStation(row: Int): Location? {
        if(this.getConfig().get("Data.Station.Rows.$row.StationSign") == null)
            return null

        val args = this.getConfig().getString("Data.Station.Rows.$row.StationSign")?.split(", ")
        val x = args?.get(0)?.toDouble()!!
        val y = args[1].toDouble()
        val z = args[2].toDouble()

        return Location(getWorld(), x, y, z)
    }

    /**
     * Sets the spawn sign of a specific row in this attraction
     */
    fun setRowSpawn(row: Int, location: Location){
        this.getConfig().set("Data.Station.Rows.$row.SpawnSign", "${location.x}, ${location.y}, ${location.z}")
        this.reloadConfig()
    }

    /**
     * Get the location of the spawn sign of a specific row
     * @return the location where a redstone signal should be given
     */
    private fun getRowSpawn(row: Int): Location? {
        if(this.getConfig().get("Data.Station.Rows.$row.SpawnSign") == null)
            return null

        val args = this.getConfig().getString("Data.Station.Rows.$row.SpawnSign")?.split(", ")
        val x = args?.get(0)?.toDouble()!!
        val y = args[1].toDouble()
        val z = args[2].toDouble()

        return Location(getWorld(), x, y, z)
    }

    /**
     * Sets the despawn sign of a specific row in this attraction
     */
    fun setRowDespawn(row: Int, location: Location){
        this.getConfig().set("Data.Station.Rows.$row.DespawnSign", "${location.x}, ${location.y}, ${location.z}")
        this.reloadConfig()
    }

    /**
     * Get the location of the despawn sign of a specific row
     * @return the location where a redstone signal should be given
     */
    private fun getRowDespawn(row: Int): Location? {
        if(this.getConfig().get("Data.Station.Rows.$row.DespawnSign") == null)
            return null

        val args = this.getConfig().getString("Data.Station.Rows.$row.DespawnSign")?.split(", ")
        val x = args?.get(0)?.toDouble()!!
        val y = args[1].toDouble()
        val z = args[2].toDouble()

        return Location(getWorld(), x, y, z)
    }

    fun activateRow(row: Int, activated: Boolean){
        if(getRowStation(row) == null) {
            Bukkit.getLogger()
                .warning("Station sign for row $row for ${getName()} in ${getCategory()} still has to be made! Please do this as quickly as possible!")
            return
        }

        if(activated)
            getRowStation(row)!!.block.type = Material.REDSTONE_TORCH
        else
            getRowStation(row)!!.block.type = Material.AIR
    }

    fun dispatch(){
        if(getRowStation(1) == null) {
            Bukkit.getLogger()
                .warning("Station sign(s) for ${getName()} in ${getCategory()} still has to be made! Please do this as quickly as possible!")
            return
        }
        getRowStation(1)!!.block.type = Material.REDSTONE_TORCH

        Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, {
            getRowStation(1)!!.block.type = Material.AIR
        }, 5L)
    }

    fun spawn(){
        for(row in getRows()) {

            if (getRowSpawn(row) == null) {
                Bukkit.getLogger()
                    .warning("Spawn sign(s) for ${getName()} in ${getCategory()} still has to be made! Please do this as quickly as possible!")
                return
            }
            getRowSpawn(row)!!.block.type = Material.REDSTONE_TORCH

            Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, {
                getRowSpawn(row)!!.block.type = Material.AIR
            }, 5L)

        }
    }

    fun despawn(){
        for(row in getRows()) {

            if (getRowDespawn(row) == null) {
                Bukkit.getLogger()
                    .warning("Depawn sign(s) for ${getName()} in ${getCategory()} still has to be made! Please do this as quickly as possible!")
                return
            }
            getRowDespawn(row)!!.block.type = Material.REDSTONE_TORCH

            Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, {
                getRowDespawn(row)!!.block.type = Material.AIR
            }, 5L)

        }
    }

}