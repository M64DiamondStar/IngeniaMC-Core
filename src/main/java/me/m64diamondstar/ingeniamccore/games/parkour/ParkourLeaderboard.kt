package me.m64diamondstar.ingeniamccore.games.parkour

import me.m64diamondstar.ingeniamccore.utils.entities.LeaderboardPacketEntity
import me.m64diamondstar.ingeniamccore.utils.leaderboard.Leaderboard
import me.m64diamondstar.ingeniamccore.utils.leaderboard.LeaderboardRegistry
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.world.level.Level
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import java.awt.Color
import java.util.*

class ParkourLeaderboard(private val parkour: Parkour) {

    /**
     * Returns the world of the attraction
     */
    private fun getNMSWorld(): Level?{
        return (parkour.world as CraftWorld).handle
    }

    /**
     * Sets the location of the leaderboard sign
     */
    fun setLeaderboardLocation(x: Int, y: Int, z: Int){
        parkour.getConfig().set("Leaderboard.Location", "$x, $y, $z")
        parkour.save()
    }

    /**
     * Set the leaderboard on enabled or disabled
     */
    fun setLeaderboardEnabled(enabled: Boolean){
        parkour.getConfig().set("Leaderboard.Enabled", enabled)
        parkour.save()
    }

    /**
     * Returns the location of the leaderboard sign
     */
    private fun getLeaderboardLocation(): BlockPos {
        val args = parkour.getConfig().getString("Leaderboard.Location")?.split(", ")
        return BlockPos(args!![0].toInt(), args[1].toInt(), args[2].toInt())
    }

    /**
     * Sets the rotation of the leaderboard sign
     */
    fun setLeaderboardDirection(direction: String){
        var realDirection = direction
        if(direction != "NORTH" && direction != "SOUTH" && direction != "WEST" && direction != "EAST")
            realDirection = "NORTH"


        parkour.getConfig().set("Leaderboard.Face", realDirection)
        parkour.save()
    }

    /**
     * Returns the rotation of the leaderboard sign. Returns NORTH if null
     */
    private fun getLeaderboardDirection(): Direction {
        try {
            if(parkour.getConfig().getString("Leaderboard.Face") == null)
                return Direction.NORTH
            return Direction.valueOf(parkour.getConfig().getString("Leaderboard.Face")!!)
        }catch (ex: IllegalArgumentException){
            return Direction.NORTH
        }
    }

    /**
     * Spawn an EntityItemFrame with top 3 and personal soaks of the current battle for given player.
     */
    fun spawnSign(player: Player){

        val leaderboard = Leaderboard(getRecordsInMap(), getLeaderboardBackgroundColor(), getLeaderboardOutlineColor(),
            getLeaderboardTitleColor(), getLeaderboardPositionColor(), getLeaderboardNameColor(), getLeaderboardLineColor(), true)

        despawnParkourSign(player)

        val leaderboardPacketEntity = LeaderboardPacketEntity(leaderboard, getNMSWorld(),
            getLeaderboardLocation(), getLeaderboardDirection())
        leaderboardPacketEntity.spawn(player, "Best Times")
        LeaderboardRegistry.setBoard(ParkourUtils.getParkourID(parkour), player, leaderboardPacketEntity.id)
    }

    /**
     * Spawn an EntityItemFrame with top 3 and personal records for every player.
     */
    fun spawnSign(){
        Bukkit.getOnlinePlayers().forEach { spawnSign(it) }
    }

    /**
     * Set the soaks of a specific player.
     */
    fun setRecord(player: OfflinePlayer, record: Long){
        if(record <= 0)
            parkour.getConfig().set("Records.${player.uniqueId}", null)
        else
            parkour.getConfig().set("Records.${player.uniqueId}", record)
        parkour.save()
    }

    /**
     * Get the soaks of a specific player.
     * @return the soak amount
     */
    fun getRecord(player: OfflinePlayer): Long{
        return parkour.getConfig().getLong("Records.${player.uniqueId}")
    }

    /**
     * Despawn the leaderboard for everyone on the server.
     */
    private fun despawnParkourSign(player: Player){
        if(LeaderboardRegistry.getId(ParkourUtils.getParkourID(parkour), player) != null)
            (player as CraftPlayer).handle.connection.send(ClientboundRemoveEntitiesPacket(LeaderboardRegistry.getId(ParkourUtils.getParkourID(parkour), player)))
    }

    /**
     * Returns the times list with player names
     */
    private fun getRecordsInMap(): Map<String, Long>{
        val map: MutableMap<String, Long> = HashMap()

        if(parkour.getConfig().getConfigurationSection("Records") == null)
            return map

        for(key in parkour.getConfig().getConfigurationSection("Records")?.getKeys(false)!!){
            if(Bukkit.getOfflinePlayer(UUID.fromString(key)).name != null)
                map[Bukkit.getOfflinePlayer(UUID.fromString(key)).name!!] = parkour.getConfig().getLong("Records.$key")
        }

        return map
    }

    /**
     * Returns the color of the leaderboard background
     */
    private fun getLeaderboardBackgroundColor(): Color {
        if(parkour.getConfig().getString("Leaderboard.Colors.Background") == null
            || Colors.getJavaColorFromString(parkour.getConfig().getString("Leaderboard.Colors.Background")!!) == null){
            parkour.getConfig().set("Leaderboard.Colors.Background", "49, 49, 49")
            parkour.save()
        }
        return Colors.getJavaColorFromString(parkour.getConfig().getString("Leaderboard.Colors.Background")!!)!!
    }

    /**
     * Returns the color of the leaderboard outline
     */
    private fun getLeaderboardOutlineColor(): Color {
        if(parkour.getConfig().getString("Leaderboard.Colors.Outline") == null
            || Colors.getJavaColorFromString(parkour.getConfig().getString("Leaderboard.Colors.Outline")!!) == null){
            parkour.getConfig().set("Leaderboard.Colors.Outline", "60, 60, 60")
            parkour.save()
        }
        return Colors.getJavaColorFromString(parkour.getConfig().getString("Leaderboard.Colors.Outline")!!)!!
    }

    /**
     * Returns the color of the leaderboard title
     */
    private fun getLeaderboardTitleColor(): Color {
        if(parkour.getConfig().getString("Leaderboard.Colors.Title") == null
            || Colors.getJavaColorFromString(parkour.getConfig().getString("Leaderboard.Colors.Title")!!) == null){
            parkour.getConfig().set("Leaderboard.Colors.Title", "153, 153, 153")
            parkour.save()
        }
        return Colors.getJavaColorFromString(parkour.getConfig().getString("Leaderboard.Colors.Title")!!)!!
    }

    /**
     * Returns the color of the leaderboard position
     */
    private fun getLeaderboardPositionColor(): Color {
        if(parkour.getConfig().getString("Leaderboard.Colors.PositionRidecount") == null
            || Colors.getJavaColorFromString(parkour.getConfig().getString("Leaderboard.Colors.PositionRidecount")!!) == null){
            parkour.getConfig().set("Leaderboard.Colors.PositionRidecount", "180, 180, 180")
            parkour.save()
        }
        return Colors.getJavaColorFromString(parkour.getConfig().getString("Leaderboard.Colors.PositionRidecount")!!)!!
    }

    /**
     * Returns the color of the leaderboard player name
     */
    private fun getLeaderboardNameColor(): Color {
        if(parkour.getConfig().getString("Leaderboard.Colors.Name") == null
            || Colors.getJavaColorFromString(parkour.getConfig().getString("Leaderboard.Colors.Name")!!) == null){
            parkour.getConfig().set("Leaderboard.Colors.Name", "250, 250, 250")
            parkour.save()
        }
        return Colors.getJavaColorFromString(parkour.getConfig().getString("Leaderboard.Colors.Name")!!)!!
    }

    /**
     * Returns the color of the leaderboard line
     */
    private fun getLeaderboardLineColor(): Color {
        if(parkour.getConfig().getString("Leaderboard.Colors.Line") == null
            || Colors.getJavaColorFromString(parkour.getConfig().getString("Leaderboard.Colors.Line")!!) == null){
            parkour.getConfig().set("Leaderboard.Colors.Line", "210, 210, 210")
            parkour.save()
        }
        return Colors.getJavaColorFromString(parkour.getConfig().getString("Leaderboard.Colors.Line")!!)!!
    }

}