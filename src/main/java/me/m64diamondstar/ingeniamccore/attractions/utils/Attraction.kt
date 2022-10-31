package me.m64diamondstar.ingeniamccore.attractions.utils

import me.m64diamondstar.ingeniamccore.Main
import me.m64diamondstar.ingeniamccore.data.Configuration
import me.m64diamondstar.ingeniamccore.utils.entities.EntityRegistry
import me.m64diamondstar.ingeniamccore.utils.entities.LeaderboardPacketEntity
import me.m64diamondstar.ingeniamccore.utils.leaderboard.Leaderboard
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import net.minecraft.core.BlockPosition
import net.minecraft.core.EnumDirection
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import java.awt.Color
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class Attraction(category: String, name: String): Configuration("rides/$category", name.replace(".yml", ""), false, false) {

    private val name: String
    private val category: String

    /**
     * Constructor for managing an attraction.
     */
    init {
        this.name = name
        this.category = category
        create()
    }

    /**
     * Creates the attraction with a specific AttractionType in a specific World
     */
    fun createAttraction(type: AttractionType, world: World){

        this.getConfig().set("Type", type.toString())
        this.getConfig().set("Name", getName())
        this.getConfig().set("DisplayName", "&c${getName()}")
        this.getConfig().set("World", world.name)

        this.getConfig().set("Show.Enabled", false)
        this.getConfig().set("Show.Category", null)
        this.getConfig().set("Show.Name", null)

        this.getConfig().set("Gates.Enabled", false)

        this.getConfig().set("Leaderboard.Enabled", false)
        this.getConfig().set("Leaderboard.Colors.Background", "49, 49, 49")
        this.getConfig().set("Leaderboard.Colors.Outline", "60, 60, 60")
        this.getConfig().set("Leaderboard.Colors.Title", "153, 153, 153")
        this.getConfig().set("Leaderboard.Colors.PositionRidecount", "180, 180, 180")
        this.getConfig().set("Leaderboard.Colors.Name", "250, 250, 250")
        this.getConfig().set("Leaderboard.Colors.Line", "210, 210, 210")

        val comments = ArrayList<String>()
        comments.add(" ")
        comments.add(" ")
        comments.add("-----------------------------------------")
        comments.add("This is the file for the attraction: ${getName()}.")
        comments.add("DO NOT EDIT ANYTHING UNDER THIS EXCEPT WHEN IT IS REALLY NECESSARY")
        comments.add("-----------------------------------------")

        this.getConfig().set("Data.RidecountID", 0)
        this.getConfig().setComments("Data", comments)

        val header = ArrayList<String>()
        header.add("-----------------------------------------")
        header.add("This is the file for the attraction: ${getName()}.")
        header.add("Please try to use in-game commands as much as possible, ")
        header.add("but if it's really necessary you can edit this file and use")
        header.add("/ig attraction reload ${getName()}")
        header.add("")
        header.add("Reminder, all the times are in ticks! 20 ticks = 1 second.")
        header.add("-----------------------------------------")

        this.getConfig().options().setHeader(header)

        this.reload()
    }

    /**
     * Returns the name of the attraction
     */
    fun getName(): String{
        return name
    }

    /**
     * Returns the category of the attraction
     */
    fun getCategory(): String{
        return category
    }

    fun getDisplayName(): String{
        if(!this.exists()){
            Bukkit.getLogger().warning("The configuration of $name in $category has not been created yet! Using Name as DisplayName")
            return name
        }
        return this.getConfig().getString("DisplayName")!!
    }

    /**
     * Returns the world of the attraction
     */
    fun getWorld(): World? {
        if(this.getConfig().getString("World") == null){
            this.getConfig().set("World", "world")
            this.reload()
        }

        return Bukkit.getWorld(this.getConfig().getString("World")!!)!!
    }

    /**
     * Returns the world of the attraction
     */
    private fun getNMSWorld(): net.minecraft.world.level.World?{
        return (getWorld() as CraftWorld).handle
    }

    private fun isEnabled(configurationSection: ConfigurationSection): Boolean{
        if(configurationSection.get("Enabled") == null)
            return false
        return configurationSection.getBoolean("Enabled")
    }

    /**
     * Sets the location of the leaderboard sign
     */
    fun setLeaderboardLocation(x: Int, y: Int, z: Int){
        this.getConfig().set("Leaderboard.Location", "$x, $y, $z")
        this.reload()
    }

    /**
     * Set the leaderboard on enabled or disabled
     */
    fun setLeaderboardEnabled(enabled: Boolean){
        this.getConfig().set("Leaderboard.Enabled", enabled)
        this.reload()
    }

    /**
     * Returns the location of the leaderboard sign
     */
    private fun getLeaderboardLocation(): BlockPosition {
        val args = this.getConfig().getString("Leaderboard.Location")?.split(", ")
        return BlockPosition(args!![0].toInt(), args[1].toInt(), args[2].toInt())
    }

    /**
     * Sets the rotation of the leaderboard sign
     */
    fun setLeaderboardDirection(direction: String){
        var realDirection = direction
        if(direction != "NORTH" && direction != "SOUTH" && direction != "WEST" && direction != "EAST")
            realDirection = "NORTH"


        this.getConfig().set("Leaderboard.Face", realDirection)
        this.reload()
    }

    /**
     * Returns the rotation of the leaderboard sign. Returns NORTH if null
     */
    private fun getLeaderboardDirection(): EnumDirection{
        var enumDirection = EnumDirection.c

        if(this.getConfig().getString("Leaderboard.Face") == "SOUTH")
            enumDirection = EnumDirection.d
        if(this.getConfig().getString("Leaderboard.Face") == "WEST")
            enumDirection = EnumDirection.e
        if(this.getConfig().getString("Leaderboard.Face") == "EAST")
            enumDirection = EnumDirection.f

        return enumDirection
    }

    /**
     * Spawn an EntityItemFrame with top 3 and personal ridecount of the current ride for given player.
     */
    fun spawnRidecountSign(player: Player){

        if(!this.exists()){
            Bukkit.getLogger().warning("The configuration of $name in $category has not been created yet! Cannot spawn frame without needed data. " +
                    "Please create this file first.")
            return
        }

        if(!isEnabled(this.getConfig().getConfigurationSection("Leaderboard")!!))
            return

        val leaderboard = Leaderboard(getRidecountInMap(), getLeaderboardBackgroundColor(), getLeaderboardOutlineColor(),
        getLeaderboardTitleColor(), getLeaderboardPositionColor(), getLeaderboardNameColor(), getLeaderboardLineColor())

        despawnRidecountSign()

        val leaderboardPacketEntity = LeaderboardPacketEntity(leaderboard, getNMSWorld(),
            getLeaderboardLocation(), getLeaderboardDirection())
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin,  { leaderboardPacketEntity.spawn(player) }, 1L)

        this.getConfig().set("Data.RidecountID", leaderboardPacketEntity.ae())
        this.reload()
    }

    /**
     * Spawn an EntityItemFrame with top 3 and personal ridecount of the current ride for every player.
     */
    fun spawnRidecountSign(){
        Bukkit.getOnlinePlayers().forEach { spawnRidecountSign(it) }
    }

    /**
     * Despawn the leaderboard for everyone on the server.
     */
    private fun despawnRidecountSign(){
        if(this.getConfig().get("Data.RidecountID") != null)
            if(EntityRegistry.containsId(this.getConfig().getInt("Data.RidecountID")))
                Bukkit.getOnlinePlayers().forEach {
                    (it as CraftPlayer).handle.b.a(PacketPlayOutEntityDestroy(this.getConfig().getInt("Data.RidecountID") /*ID*/))
                }
    }

    /**
     * Returns the ridecount list
     */
    private fun getRidecountInMap(): Map<String, Int>{
        val map: MutableMap<String, Int> = HashMap()

        if(this.getConfig().getConfigurationSection("Data.Ridecount") == null)
            return map

        for(key in this.getConfig().getConfigurationSection("Data.Ridecount")?.getKeys(false)!!){
            map[Bukkit.getOfflinePlayer(UUID.fromString(key)).name!!] = this.getConfig().getInt("Data.Ridecount.$key")
        }

        return map
    }

    /**
     * Returns the color of the leaderboard background
     */
    private fun getLeaderboardBackgroundColor(): Color{
        if(this.getConfig().getString("Leaderboard.Colors.Background") == null){
            this.getConfig().set("Leaderboard.Colors.Background", "49, 49, 49")
            this.reload()
        }
        return Colors.getJavaColorFromString(this.getConfig().getString("Leaderboard.Colors.Background")!!)
    }

    /**
     * Returns the color of the leaderboard outline
     */
    private fun getLeaderboardOutlineColor(): Color{
        if(this.getConfig().getString("Leaderboard.Colors.Outline") == null){
            this.getConfig().set("Leaderboard.Colors.Outline", "60, 60, 60")
            this.reload()
        }
        return Colors.getJavaColorFromString(this.getConfig().getString("Leaderboard.Colors.Outline")!!)
    }

    /**
     * Returns the color of the leaderboard title
     */
    private fun getLeaderboardTitleColor(): Color{
        if(this.getConfig().getString("Leaderboard.Colors.Title") == null){
            this.getConfig().set("Leaderboard.Colors.Title", "153, 153, 153")
            this.reload()
        }
        return Colors.getJavaColorFromString(this.getConfig().getString("Leaderboard.Colors.Title")!!)
    }

    /**
     * Returns the color of the leaderboard position
     */
    private fun getLeaderboardPositionColor(): Color{
        if(this.getConfig().getString("Leaderboard.Colors.PositionRidecount") == null){
            this.getConfig().set("Leaderboard.Colors.PositionRidecount", "180, 180, 180")
            this.reload()
        }
        return Colors.getJavaColorFromString(this.getConfig().getString("Leaderboard.Colors.PositionRidecount")!!)
    }

    /**
     * Returns the color of the leaderboard player name
     */
    private fun getLeaderboardNameColor(): Color{
        if(this.getConfig().getString("Leaderboard.Colors.Name") == null){
            this.getConfig().set("Leaderboard.Colors.Name", "250, 250, 250")
            this.reload()
        }
        return Colors.getJavaColorFromString(this.getConfig().getString("Leaderboard.Colors.Name")!!)
    }

    /**
     * Returns the color of the leaderboard line
     */
    private fun getLeaderboardLineColor(): Color{
        if(this.getConfig().getString("Leaderboard.Colors.Line") == null){
            this.getConfig().set("Leaderboard.Colors.Line", "210, 210, 210")
            this.reload()
        }
        return Colors.getJavaColorFromString(this.getConfig().getString("Leaderboard.Colors.Line")!!)
    }

}