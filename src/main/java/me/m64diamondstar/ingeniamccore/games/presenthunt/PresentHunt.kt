package me.m64diamondstar.ingeniamccore.games.presenthunt

import me.m64diamondstar.ingeniamccore.data.LoadedConfiguration
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import me.m64diamondstar.ingeniamccore.utils.items.Items
import net.minecraft.core.BlockPosition
import net.minecraft.world.level.block.entity.TileEntitySkull
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Rotatable
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld
import kotlin.random.Random

class PresentHunt(val category: String, val name: String): LoadedConfiguration("games/presenthunt/$category", name, false, true)  {

    /**
     * Generate the config file
     */
    fun createPresentHunt(world: World){
        this.getConfig().set("World", world.name)

        val list = ArrayList<String>()

        list.add("This is a game where you have to hunt heads on armor stands, like present hunt.")
        list.add("Please try to use in-game commands as much as possible!")

        this.getConfig().options().setHeader(list)

        this.reloadConfig()
    }

    /**
     * Sets the world for the objects to spawn
     */
    fun setWorld(world: World){
        this.getConfig().set("World", world.name)
        this.reloadConfig()
    }

    /**
     * @return the world that the objects spawn in
     */
    fun getWorld(): World {
        return Bukkit.getWorld(this.getConfig().getString("World")!!)!!
    }

    /**
     * Add a present location to the configuration file.
     */
    fun addLocation(location: Location){
        val list = this.getConfig().getStringList("Locations")
        list.add("${location.blockX}, ${location.blockY}, ${location.blockZ}")
        this.getConfig().set("Locations", list)
        this.reloadConfig()
    }

    /**
     * Remove a present location from the configuration file.
     */
    fun removeLocation(location: Location): Boolean{
        val list = this.getConfig().getStringList("Locations")
        val success = list.remove("${location.blockX}, ${location.blockY}, ${location.blockZ}")
        this.getConfig().set("Locations", list)
        this.reloadConfig()
        return success
    }

    /**
     * Get all present locations
     * @return The list with all the locations
     */
    fun getLocations(): List<Location>{
        val locations = ArrayList<Location>()
        for(location in this.getConfig().getStringList("Locations")){
            LocationUtils.getLocationFromString("${getWorld().name}, $location")?.let { locations.add(it) }
        }
        return locations
    }

    /**
     * Spawn a present at one of the locations.
     */
    fun spawnRandomPresent(){
        if(getLocations().isEmpty()) return
        var randomLocation = getLocations()[Random.nextInt(getLocations().size)]
        var tries = 1
        while(PresentHuntUtils.containsActivePresent(randomLocation)){
            randomLocation = getLocations()[Random.nextInt(getLocations().size)]
            tries++

            if(tries >= getLocations().size)
                return
        }

        val block = randomLocation.block
        block.type = Material.PLAYER_HEAD

        val tileEntitySkull = (block.world as CraftWorld).handle.getBlockEntity(BlockPosition(block.x, block.y, block.z), true) as TileEntitySkull
        tileEntitySkull.a(Items.getRandomPresentProfile())

        val faces = BlockFace.values().toMutableList()
        faces.remove(BlockFace.DOWN)
        faces.remove(BlockFace.UP)
        faces.remove(BlockFace.SELF)

        val randomRotation = faces.shuffled().first()
        val blockData = (block.blockData as Rotatable)
        blockData.rotation = randomRotation

        block.blockData = blockData

        block.state.update()

        PresentHuntUtils.addActivePresent(location = randomLocation, this)
    }

    fun saveActivePresents(){

        getLocations().forEach {
            if(PresentHuntUtils.containsActivePresent(it)){
                val list = this.getConfig().getStringList("Saved-Locations")
                list.add("${it.blockX}, ${it.blockY}, ${it.blockZ}")
                this.getConfig().set("Saved-Locations", list)
            }
        }

        this.reloadConfig()
    }

    fun loadActivePresents(){
        val locations = ArrayList<Location>()
        for(location in this.getConfig().getStringList("Saved-Locations")){
            LocationUtils.getLocationFromString("${getWorld().name}, $location")?.let { locations.add(it) }
        }

        locations.forEach {
            PresentHuntUtils.addActivePresent(it, this)
        }

        this.getConfig().set("Saved-Locations", null)
        this.reloadConfig()
    }

}