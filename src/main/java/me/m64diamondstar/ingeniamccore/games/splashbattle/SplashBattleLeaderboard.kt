package me.m64diamondstar.ingeniamccore.games.splashbattle

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

class SplashBattleLeaderboard(private val splashBattle: SplashBattle) {

    /**
     * Returns the world of the attraction
     */
    private fun getNMSWorld(): Level?{
        return (splashBattle.world as CraftWorld).handle
    }

    /**
     * Sets the location of the leaderboard sign
     */
    fun setLeaderboardLocation(x: Int, y: Int, z: Int){
        splashBattle.getConfig().set("Leaderboard.Location", "$x, $y, $z")
        splashBattle.reloadConfig()
    }

    /**
     * Set the leaderboard on enabled or disabled
     */
    fun setLeaderboardEnabled(enabled: Boolean){
        splashBattle.getConfig().set("Leaderboard.Enabled", enabled)
        splashBattle.reloadConfig()
    }

    /**
     * Returns the location of the leaderboard sign
     */
    private fun getLeaderboardLocation(): BlockPos {
        val args = splashBattle.getConfig().getString("Leaderboard.Location")?.split(", ")
        return BlockPos(args!![0].toInt(), args[1].toInt(), args[2].toInt())
    }

    /**
     * Sets the rotation of the leaderboard sign
     */
    fun setLeaderboardDirection(direction: String){
        var realDirection = direction
        if(direction != "NORTH" && direction != "SOUTH" && direction != "WEST" && direction != "EAST")
            realDirection = "NORTH"


        splashBattle.getConfig().set("Leaderboard.Face", realDirection)
        splashBattle.reloadConfig()
    }

    /**
     * Returns the rotation of the leaderboard sign. Returns NORTH if null
     */
    private fun getLeaderboardDirection(): Direction {
        try {
            if(splashBattle.getConfig().getString("Leaderboard.Face") == null)
                return Direction.NORTH
            return Direction.valueOf(splashBattle.getConfig().getString("Leaderboard.Face")!!)
        }catch (ex: IllegalArgumentException){
            return Direction.NORTH
        }
    }

    /**
     * Spawn an EntityItemFrame with top 3 and personal soaks of the current battle for given player.
     */
    fun spawnSoaksSign(player: Player){

        if(!splashBattle.existsConfig()){
            Bukkit.getLogger().warning("The configuration of ${splashBattle.name} has not been created yet! Cannot spawn frame without needed data. " +
                    "Please create this file first.")
            return
        }

        val leaderboard = Leaderboard(getSoaksInMap(), getLeaderboardBackgroundColor(), getLeaderboardOutlineColor(),
            getLeaderboardTitleColor(), getLeaderboardPositionColor(), getLeaderboardNameColor(), getLeaderboardLineColor(), false)

        despawnSoaksSign(player)

        val leaderboardPacketEntity = LeaderboardPacketEntity(leaderboard, getNMSWorld(),
            getLeaderboardLocation(), getLeaderboardDirection())
        leaderboardPacketEntity.spawn(player, "Top Soaks")
        LeaderboardRegistry.setBoard(splashBattle.name, player, leaderboardPacketEntity.id)
    }

    /**
     * Spawn an EntityItemFrame with top 3 and personal soaks of the current battle for every player.
     */
    fun spawnSoaksSign(){
        Bukkit.getOnlinePlayers().forEach { spawnSoaksSign(it) }
    }

    /**
     * Set the soaks of a specific player.
     */
    private fun setSoaks(player: OfflinePlayer, count: Int){
        if(count <= 0)
            splashBattle.getConfig().set("Soaks.${player.uniqueId}", null)
        else
            splashBattle.getConfig().set("Soaks.${player.uniqueId}", count)
        splashBattle.reloadConfig()
    }

    /**
     * Add soaks to a specific player.
     */
    fun addSoaks(player: OfflinePlayer, count: Int){
        setSoaks(player, getSoaks(player) + count)
    }

    /**
     * Get the soaks of a specific player.
     * @return the soak amount
     */
    private fun getSoaks(player: OfflinePlayer): Int{
        return splashBattle.getConfig().getInt("Soaks.${player.uniqueId}")
    }

    /**
     * Despawn the leaderboard for everyone on the server.
     */
    private fun despawnSoaksSign(player: Player){
        if(LeaderboardRegistry.getId(splashBattle.name, player) != null)
            (player as CraftPlayer).handle.connection.send(ClientboundRemoveEntitiesPacket(LeaderboardRegistry.getId(splashBattle.name, player)))
    }

    /**
     * Returns the soaks list with player names
     */
    private fun getSoaksInMap(): Map<String, Long>{
        val map: MutableMap<String, Long> = HashMap()

        if(splashBattle.getConfig().getConfigurationSection("Soaks") == null)
            return map

        for(key in splashBattle.getConfig().getConfigurationSection("Soaks")?.getKeys(false)!!){
            if(Bukkit.getOfflinePlayer(UUID.fromString(key)).name != null)
                map[Bukkit.getOfflinePlayer(UUID.fromString(key)).name!!] = splashBattle.getConfig().getLong("Soaks.$key")
        }

        return map
    }

    /**
     * Returns the color of the leaderboard background
     */
    private fun getLeaderboardBackgroundColor(): Color {
        if(splashBattle.getConfig().getString("Leaderboard.Colors.Background") == null
            || Colors.getJavaColorFromString(splashBattle.getConfig().getString("Leaderboard.Colors.Background")!!) == null){
            splashBattle.getConfig().set("Leaderboard.Colors.Background", "49, 49, 49")
            splashBattle.reloadConfig()
        }
        return Colors.getJavaColorFromString(splashBattle.getConfig().getString("Leaderboard.Colors.Background")!!)!!
    }

    /**
     * Returns the color of the leaderboard outline
     */
    private fun getLeaderboardOutlineColor(): Color {
        if(splashBattle.getConfig().getString("Leaderboard.Colors.Outline") == null
            || Colors.getJavaColorFromString(splashBattle.getConfig().getString("Leaderboard.Colors.Outline")!!) == null){
            splashBattle.getConfig().set("Leaderboard.Colors.Outline", "60, 60, 60")
            splashBattle.reloadConfig()
        }
        return Colors.getJavaColorFromString(splashBattle.getConfig().getString("Leaderboard.Colors.Outline")!!)!!
    }

    /**
     * Returns the color of the leaderboard title
     */
    private fun getLeaderboardTitleColor(): Color {
        if(splashBattle.getConfig().getString("Leaderboard.Colors.Title") == null
            || Colors.getJavaColorFromString(splashBattle.getConfig().getString("Leaderboard.Colors.Title")!!) == null){
            splashBattle.getConfig().set("Leaderboard.Colors.Title", "153, 153, 153")
            splashBattle.reloadConfig()
        }
        return Colors.getJavaColorFromString(splashBattle.getConfig().getString("Leaderboard.Colors.Title")!!)!!
    }

    /**
     * Returns the color of the leaderboard position
     */
    private fun getLeaderboardPositionColor(): Color {
        if(splashBattle.getConfig().getString("Leaderboard.Colors.PositionRidecount") == null
            || Colors.getJavaColorFromString(splashBattle.getConfig().getString("Leaderboard.Colors.PositionRidecount")!!) == null){
            splashBattle.getConfig().set("Leaderboard.Colors.PositionRidecount", "180, 180, 180")
            splashBattle.reloadConfig()
        }
        return Colors.getJavaColorFromString(splashBattle.getConfig().getString("Leaderboard.Colors.PositionRidecount")!!)!!
    }

    /**
     * Returns the color of the leaderboard player name
     */
    private fun getLeaderboardNameColor(): Color {
        if(splashBattle.getConfig().getString("Leaderboard.Colors.Name") == null
            || Colors.getJavaColorFromString(splashBattle.getConfig().getString("Leaderboard.Colors.Name")!!) == null){
            splashBattle.getConfig().set("Leaderboard.Colors.Name", "250, 250, 250")
            splashBattle.reloadConfig()
        }
        return Colors.getJavaColorFromString(splashBattle.getConfig().getString("Leaderboard.Colors.Name")!!)!!
    }

    /**
     * Returns the color of the leaderboard line
     */
    private fun getLeaderboardLineColor(): Color {
        if(splashBattle.getConfig().getString("Leaderboard.Colors.Line") == null
            || Colors.getJavaColorFromString(splashBattle.getConfig().getString("Leaderboard.Colors.Line")!!) == null){
            splashBattle.getConfig().set("Leaderboard.Colors.Line", "210, 210, 210")
            splashBattle.reloadConfig()
        }
        return Colors.getJavaColorFromString(splashBattle.getConfig().getString("Leaderboard.Colors.Line")!!)!!
    }

}