package me.m64diamondstar.ingeniamccore.attractions.custom

import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.attractions.utils.Attraction
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionType
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.scheduler.BukkitRunnable

/**
 * Special coaster kind of ride that disables gravity on spawn and
 * enables it when a player enters. This makes the ride start instantly
 * without a countdown. Used for example at the Sled ride at the Winter Event.
 */
class Slide(category: String, name: String): Attraction(category, name) {

    companion object {

        fun clickEvent(entity: Entity) {

            if (MinecartMemberStore.getFromEntity(entity) == null) return

            val member = MinecartMemberStore.getFromEntity(entity)

            for (block in member.group.signTracker.activeTrackedSigns) {
                if (block.sign.sign.getLine(1).equals("slide", ignoreCase = true)) {

                    val attraction = Attraction(block.sign.sign.getLine(2), block.sign.sign.getLine(3))
                    if (attraction.getType() != AttractionType.SLIDE) return

                    if(attraction.getShow() != null)
                        attraction.getShow()!!.play()


                    object: BukkitRunnable(){

                        var c = 0

                        override fun run() {
                            if(c == 50){
                                this.cancel()
                                return
                            }

                            if(member.group.hasPassenger()){
                                member.group.properties.isSlowingDown = true
                                member.group.properties.playersEnter = false
                                member.group.properties.playersExit = false
                                this.cancel()
                                return
                            }

                            c++
                        }
                    }.runTaskTimer(IngeniaMC.plugin, 0L, 1L)
                }
            }
        }
    }

    fun setSpawn(location: Location){
        this.getConfig().set("Data.SpawnSign", "${location.x}, ${location.y}, ${location.z}")
        this.reloadConfig()
    }

    private fun getSpawn(): Location? {
        if(this.getConfig().get("Data.SpawnSign") == null)
            return null

        val args = this.getConfig().getString("Data.SpawnSign")?.split(", ")
        val x = args?.get(0)?.toDouble()!!
        val y = args[1].toDouble()
        val z = args[2].toDouble()

        return Location(getWorld(), x, y, z)
    }

    fun setDespawn(location: Location){
        this.getConfig().set("Data.DespawnSign", "${location.x}, ${location.y}, ${location.z}")
        this.reloadConfig()
    }

    private fun getDespawn(): Location? {
        if(this.getConfig().get("Data.DespawnSign") == null)
            return null

        val args = this.getConfig().getString("Data.DespawnSign")?.split(", ")
        val x = args?.get(0)?.toDouble()!!
        val y = args[1].toDouble()
        val z = args[2].toDouble()

        return Location(getWorld(), x, y, z)
    }

    fun spawn(){
        if (getSpawn() == null) {
            Bukkit.getLogger()
                .warning("Spawn sign for ${getName()} in ${getCategory()} still has to be made! Please do this as quickly as possible!")
            return
        }
        getSpawn()!!.block.type = Material.REDSTONE_TORCH

        Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, {
            getSpawn()!!.block.type = Material.AIR
        }, 5L)
    }

    fun despawn(){
        if (getDespawn() == null) {
            Bukkit.getLogger()
                .warning("Despawn sign for ${getName()} in ${getCategory()} still has to be made! Please do this as quickly as possible!")
            return
        }
        getDespawn()!!.block.type = Material.REDSTONE_TORCH

        Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, {
            getDespawn()!!.block.type = Material.AIR
        }, 5L)
    }

}