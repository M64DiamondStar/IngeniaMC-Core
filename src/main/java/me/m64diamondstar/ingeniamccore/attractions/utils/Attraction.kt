package me.m64diamondstar.ingeniamccore.attractions.utils

import me.m64diamondstar.ingeniamccore.Main
import me.m64diamondstar.ingeniamccore.data.Configuration
import me.m64diamondstar.ingeniamccore.utils.entities.EntityRegistry
import me.m64diamondstar.ingeniamccore.utils.entities.LeaderboardPacketEntity
import me.m64diamondstar.ingeniamccore.utils.items.ItemDecoder
import me.m64diamondstar.ingeniamccore.utils.items.ItemEncoder
import me.m64diamondstar.ingeniamccore.utils.leaderboard.Leaderboard
import me.m64diamondstar.ingeniamccore.utils.leaderboard.LeaderboardRegistry
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import net.minecraft.core.BlockPosition
import net.minecraft.core.EnumDirection
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.awt.Color
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class Attraction(category: String, name: String): Configuration("rides/$category", name.replace(".yml", ""), false, true) {

    private val name: String
    private val category: String

    /**
     * Constructor for managing an attraction.
     */
    init {
        this.name = name
        this.category = category
    }

    /**
     * Creates the attraction with a specific AttractionType in a specific World
     */
    fun createAttraction(type: AttractionType, world: World){

        this.getConfig().set("Type", type.toString())
        this.getConfig().set("Name", getName())
        this.getConfig().set("DisplayName", "&c${getName()}")
        this.getConfig().set("World", world.name)

        this.getConfig().set("Warp.Enabled", false)

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

        this.getConfig().set("Data", " ")
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

        this.reloadConfig()
    }

    /**
     * @return the name of the attraction
     */
    fun getName(): String{
        return name
    }

    /**
     * @return the type of the attraction
     */
    fun getType(): AttractionType? {
        return this.getConfig().getString("Type")?.let { AttractionType.valueOf(it) }
    }

    /**
     * Returns the category of the attraction
     */
    fun getCategory(): String{
        return category
    }

    /**
     * Returns the world of the attraction
     */
    fun getWorld(): World? {
        if(this.getConfig().getString("World") == null){
            this.getConfig().set("World", "world")
            this.reloadConfig()
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
        this.reloadConfig()
    }

    /**
     * Set the leaderboard on enabled or disabled
     */
    fun setLeaderboardEnabled(enabled: Boolean){
        this.getConfig().set("Leaderboard.Enabled", enabled)
        this.reloadConfig()
    }

    /**
     * Set the warp location
     * @return is last setting to enable warp?
     */
    fun setWarpLocation(location: Location): Boolean{
        this.getConfig().set("Warp.Location", "${location.x}, ${location.y}, ${location.z}, ${location.yaw}, ${location.pitch}")
        if(this.getConfig().get("Warp.Item") != null) {
            this.getConfig().set("warp.Enabled", true)
            this.reloadConfig()
            return true
        }

        this.reloadConfig()
        return false
    }

    /**
     * Set the warp item
     * @return is last setting to enable warp?
     */
    fun setWarpItem(itemStack: ItemStack): Boolean{
        this.getConfig().set("Warp.Item", "${ItemEncoder(itemStack).encodedItem()}")
        if(this.getConfig().get("Warp.Location") != null) {
            this.getConfig().set("warp.Enabled", true)
            this.reloadConfig()
            return true
        }

        this.reloadConfig()
        return false
    }

    /**
     * Get the warp location
     */
    fun getWarpLocation(): Location?{
        if(this.getConfig().get("Warp.Location") == null)
            return null

        val args = this.getConfig().getString("Warp.Location")?.split(", ")
        val x = args?.get(0)?.toDouble()!!
        val y = args[1].toDouble()
        val z = args[2].toDouble()

        val yaw = args[3].toFloat()
        val pitch = args[4].toFloat()

        return Location(getWorld(), x, y, z, yaw, pitch)
    }

    /**
     * Get the warp item that displays in the warp menu
     */
    fun getWarpItem(): ItemStack?{
        if(this.getConfig().get("Warp.Item") == null)
            return null

        return ItemDecoder(this.getConfig().getString("Warp.Item")!!).decodedItem()
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
        this.reloadConfig()
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

        if(!this.existsConfig()){
            Bukkit.getLogger().warning("The configuration of $name in $category has not been created yet! Cannot spawn frame without needed data. " +
                    "Please create this file first.")
            return
        }

        if(!isEnabled(this.getConfig().getConfigurationSection("Leaderboard")!!))
            return

        val leaderboard = Leaderboard(getRidecountInMap(), getLeaderboardBackgroundColor(), getLeaderboardOutlineColor(),
        getLeaderboardTitleColor(), getLeaderboardPositionColor(), getLeaderboardNameColor(), getLeaderboardLineColor())

        despawnRidecountSign(player)

        val leaderboardPacketEntity = LeaderboardPacketEntity(leaderboard, getNMSWorld(),
            getLeaderboardLocation(), getLeaderboardDirection())
        leaderboardPacketEntity.spawn(player)
        LeaderboardRegistry.setBoard(player, leaderboardPacketEntity.ae())
    }

    /**
     * Spawn an EntityItemFrame with top 3 and personal ridecount of the current ride for every player.
     */
    fun spawnRidecountSign(){
        Bukkit.getOnlinePlayers().forEach { spawnRidecountSign(it) }
    }

    /**
     * Set the ridecount of a specific player.
     */
    fun setRidecount(player: OfflinePlayer, count: Int){
        this.getConfig().set("Data.Ridecount.${player.uniqueId}.Count", count)
        this.reloadConfig()
    }

    fun addRidecount(player: OfflinePlayer, count: Int){
        setRidecount(player, getRidecount(player) + count)
    }

    fun getRidecount(player: OfflinePlayer): Int{
        return this.getConfig().getInt("Data.Ridecount.${player.uniqueId}.Count")
    }

    /**
     * Despawn the leaderboard for everyone on the server.
     */
    private fun despawnRidecountSign(player: Player){
        if(LeaderboardRegistry.getBoards().containsKey(player.uniqueId))
            (player as CraftPlayer).handle.b.a(LeaderboardRegistry.getBoards()[player.uniqueId]?.let {
                PacketPlayOutEntityDestroy(
                    it /*ID*/)
            })
    }

    /**
     * Returns the ridecount list with player names
     */
    fun getRidecountInMap(): Map<String, Int>{
        val map: MutableMap<String, Int> = HashMap()

        if(this.getConfig().getConfigurationSection("Data.Ridecount") == null)
            return map

        for(key in this.getConfig().getConfigurationSection("Data.Ridecount")?.getKeys(false)!!){
            map[Bukkit.getOfflinePlayer(UUID.fromString(key)).name!!] = this.getConfig().getInt("Data.Ridecount.$key.Count")
        }

        return map
    }

    /**
     * Returns the ridecount list with player uuids
     */
    fun getRidecountInMapUuid(): Map<UUID, Int>{
        val map: MutableMap<UUID, Int> = HashMap()

        if(this.getConfig().getConfigurationSection("Data.Ridecount") == null)
            return map

        for(key in this.getConfig().getConfigurationSection("Data.Ridecount")?.getKeys(false)!!){
            map[UUID.fromString(key)] = this.getConfig().getInt("Data.Ridecount.$key.Count")
        }

        return map
    }

    /**
     * Returns the color of the leaderboard background
     */
    private fun getLeaderboardBackgroundColor(): Color{
        if(this.getConfig().getString("Leaderboard.Colors.Background") == null){
            this.getConfig().set("Leaderboard.Colors.Background", "49, 49, 49")
            this.reloadConfig()
        }
        return Colors.getJavaColorFromString(this.getConfig().getString("Leaderboard.Colors.Background")!!)
    }

    /**
     * Returns the color of the leaderboard outline
     */
    private fun getLeaderboardOutlineColor(): Color{
        if(this.getConfig().getString("Leaderboard.Colors.Outline") == null){
            this.getConfig().set("Leaderboard.Colors.Outline", "60, 60, 60")
            this.reloadConfig()
        }
        return Colors.getJavaColorFromString(this.getConfig().getString("Leaderboard.Colors.Outline")!!)
    }

    /**
     * Returns the color of the leaderboard title
     */
    private fun getLeaderboardTitleColor(): Color{
        if(this.getConfig().getString("Leaderboard.Colors.Title") == null){
            this.getConfig().set("Leaderboard.Colors.Title", "153, 153, 153")
            this.reloadConfig()
        }
        return Colors.getJavaColorFromString(this.getConfig().getString("Leaderboard.Colors.Title")!!)
    }

    /**
     * Returns the color of the leaderboard position
     */
    private fun getLeaderboardPositionColor(): Color{
        if(this.getConfig().getString("Leaderboard.Colors.PositionRidecount") == null){
            this.getConfig().set("Leaderboard.Colors.PositionRidecount", "180, 180, 180")
            this.reloadConfig()
        }
        return Colors.getJavaColorFromString(this.getConfig().getString("Leaderboard.Colors.PositionRidecount")!!)
    }

    /**
     * Returns the color of the leaderboard player name
     */
    private fun getLeaderboardNameColor(): Color{
        if(this.getConfig().getString("Leaderboard.Colors.Name") == null){
            this.getConfig().set("Leaderboard.Colors.Name", "250, 250, 250")
            this.reloadConfig()
        }
        return Colors.getJavaColorFromString(this.getConfig().getString("Leaderboard.Colors.Name")!!)
    }

    /**
     * Returns the color of the leaderboard line
     */
    private fun getLeaderboardLineColor(): Color{
        if(this.getConfig().getString("Leaderboard.Colors.Line") == null){
            this.getConfig().set("Leaderboard.Colors.Line", "210, 210, 210")
            this.reloadConfig()
        }
        return Colors.getJavaColorFromString(this.getConfig().getString("Leaderboard.Colors.Line")!!)
    }

}