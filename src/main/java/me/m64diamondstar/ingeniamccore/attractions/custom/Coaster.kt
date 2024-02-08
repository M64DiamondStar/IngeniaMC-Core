package me.m64diamondstar.ingeniamccore.attractions.custom

import com.bergerkiller.bukkit.tc.controller.MinecartGroup
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.attractions.Attraction
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionType
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.scheduler.BukkitRunnable

/**
 * Used for a lot of normal coasters and also special in the park.
 */
class Coaster(category: String, name: String): Attraction(category, name) {

    companion object {
        private val usedCoaster = java.util.ArrayList<MinecartGroup>()

        fun clickEvent(entity: Entity){

            if(MinecartMemberStore.getFromEntity(entity) == null) return

            val member = MinecartMemberStore.getFromEntity(entity)

            if(usedCoaster.contains(member.group)) return

            for(block in member.group.signTracker.activeTrackedSigns) {
                if (block.sign.sign.getLine(1).contains("attraction")) {
                    if (block.sign.sign.getLine(1).split(" ")[1].toInt() != 1) return

                    usedCoaster.add(member.group)

                    val attraction = Attraction(block.sign.sign.getLine(2), block.sign.sign.getLine(3))
                    if (attraction.getType() != AttractionType.COASTER) return

                    object : BukkitRunnable() {

                        var c = attraction.getCountdownTime()

                        override fun run() {
                            if (!member.group.hasPassenger()) {
                                usedCoaster.remove(member.group)
                                this.cancel()
                                return
                            }

                            if(member.isUnloaded){
                                this.cancel()
                                return
                            }

                            if (c == 0) {
                                member.group.properties.playersEnter = false
                                member.group.properties.playersExit = false

                                if (attraction.getType() == AttractionType.COASTER) {
                                    val coaster = Coaster(attraction.getCategory(), attraction.getName())
                                    coaster.dispatch()
                                }

                                usedCoaster.remove(member.group)
                                attraction.closeGates()

                                this.cancel()
                                return
                            }

                            for (members in member.group)
                                members.entity.playerPassengers.forEach {
                                    attraction.getCountdownType().sendActionBarMessage(player = it, c)
                                }
                            c--
                        }
                    }.runTaskTimer(IngeniaMC.plugin, 0L, 20L)
                }
            }
        }
    }

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
            getRowStation(row)!!.block.setType(Material.REDSTONE_TORCH, true)
        else
            getRowStation(row)!!.block.setType(Material.AIR, true)
    }

    fun dispatch(){
        if(getShow() != null) getShow()!!.play()

        if(getRowStation(1) == null) {
            Bukkit.getLogger()
                .warning("Station sign(s) for ${getName()} in ${getCategory()} still has to be made! Please do this as quickly as possible!")
            return
        }
        getRowStation(1)!!.block.setType(Material.REDSTONE_TORCH, true)

        Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, {
            getRowStation(1)!!.block.setType(Material.AIR, true)
        }, 5L)
    }

    fun spawn(){
        for(row in getRows()) {

            if (getRowSpawn(row) == null) {
                Bukkit.getLogger()
                    .warning("Spawn sign(s) for ${getName()} in ${getCategory()} still has to be made! Please do this as quickly as possible!")
                return
            }
            getRowSpawn(row)!!.block.setType(Material.REDSTONE_TORCH, true)

            getRowStation(row)!!.block.setType(Material.AIR, true)
            setRowOccupied(row, true)

            Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, {
                getRowSpawn(row)!!.block.setType(Material.AIR, true)
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

            getRowDespawn(row)!!.block.setType(Material.REDSTONE_TORCH, true)

            getRowStation(row)!!.block.setType(Material.AIR, true)
            setRowOccupied(row, true) //Prevent bugs at next spawn

            Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, {
                getRowDespawn(row)!!.block.setType(Material.AIR, true)
            }, 5L)

        }
    }

}