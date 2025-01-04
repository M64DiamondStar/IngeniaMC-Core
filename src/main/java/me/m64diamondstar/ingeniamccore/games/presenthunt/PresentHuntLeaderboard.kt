package me.m64diamondstar.ingeniamccore.games.presenthunt

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
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player
import java.awt.Color
import java.util.UUID

class PresentHuntLeaderboard(private val presentHunt: PresentHunt) {

    /**
     * Returns the world of the attraction
     */
    private fun getNMSWorld(): Level?{
        return (presentHunt.getWorld() as CraftWorld).handle
    }

    /**
     * Sets the location of the leaderboard sign
     */
    fun setLeaderboardLocation(x: Int, y: Int, z: Int){
        presentHunt.getConfig().set("Leaderboard.Location", "$x, $y, $z")
        presentHunt.save()
    }

    /**
     * Set the leaderboard on enabled or disabled
     */
    fun setLeaderboardEnabled(enabled: Boolean){
        presentHunt.getConfig().set("Leaderboard.Enabled", enabled)
        presentHunt.save()
    }

    /**
     * Returns the location of the leaderboard sign
     */
    private fun getLeaderboardLocation(): BlockPos {
        val args = presentHunt.getConfig().getString("Leaderboard.Location")?.split(", ")
        return BlockPos(args!![0].toInt(), args[1].toInt(), args[2].toInt())
    }

    /**
     * Sets the rotation of the leaderboard sign
     */
    fun setLeaderboardDirection(direction: String){
        var realDirection = direction
        if(direction != "NORTH" && direction != "SOUTH" && direction != "WEST" && direction != "EAST")
            realDirection = "NORTH"


        presentHunt.getConfig().set("Leaderboard.Face", realDirection)
        presentHunt.save()
    }

    /**
     * Returns the rotation of the leaderboard sign. Returns NORTH if null
     */
    private fun getLeaderboardDirection(): Direction {
        try {
            if(presentHunt.getConfig().getString("Leaderboard.Face") == null)
                return Direction.NORTH
            return Direction.valueOf(presentHunt.getConfig().getString("Leaderboard.Face")!!)
        }catch (ex: IllegalArgumentException){
            return Direction.NORTH
        }
    }

    /**
     * Spawn an EntityItemFrame with top 3 and personal soaks of the current battle for given player.
     */
    fun spawnSign(player: Player){

        if(presentHunt.getConfig().getString("Leaderboard.Location") == null) return

        val leaderboard = Leaderboard(getRecordsInMap(), getLeaderboardBackgroundColor(), getLeaderboardOutlineColor(),
            getLeaderboardTitleColor(), getLeaderboardPositionColor(), getLeaderboardNameColor(), getLeaderboardLineColor(), false)

        despawnParkourSign(player)

        if(getNMSWorld() == null) return

        val leaderboardPacketEntity = LeaderboardPacketEntity(leaderboard, getNMSWorld()!!,
            getLeaderboardLocation(), getLeaderboardDirection())
        leaderboardPacketEntity.spawn(player, "Presents Found")
        LeaderboardRegistry.setBoard(PresentHuntUtils.getPresentHuntID(presentHunt), player, leaderboardPacketEntity.id)
    }

    /**
     * Spawn an EntityItemFrame with top 3 and personal records for every player.
     */
    fun spawnSign(){
        Bukkit.getOnlinePlayers().forEach { spawnSign(it) }
    }

    /**
     * Get the soaks of a specific player.
     * @return the soak amount
     */
    fun getRecord(player: OfflinePlayer): Long{
        return presentHunt.getPlayerPresents(player.uniqueId)
    }

    /**
     * Despawn the leaderboard for everyone on the server.
     */
    private fun despawnParkourSign(player: Player){
        if(LeaderboardRegistry.getId(PresentHuntUtils.getPresentHuntID(presentHunt), player) != null)
            (player as CraftPlayer).handle.connection.send(ClientboundRemoveEntitiesPacket(LeaderboardRegistry.getId(PresentHuntUtils.getPresentHuntID(presentHunt), player)))
    }

    /**
     * Returns the times list with player names
     */
    private fun getRecordsInMap(): Map<String, Long>{
        val map: MutableMap<String, Long> = HashMap()

        if(presentHunt.getConfig().getConfigurationSection("players") == null)
            return map

        for(key in presentHunt.getConfig().getConfigurationSection("players")?.getKeys(false)!!){
            if(Bukkit.getOfflinePlayer(UUID.fromString(key)).name != null)
                map[Bukkit.getOfflinePlayer(UUID.fromString(key)).name!!] = presentHunt.getConfig().getLong("players.$key")
        }

        return map
    }

    /**
     * Returns the color of the leaderboard background
     */
    private fun getLeaderboardBackgroundColor(): Color {
        if(presentHunt.getConfig().getString("Leaderboard.Colors.Background") == null
            || Colors.getJavaColorFromString(presentHunt.getConfig().getString("Leaderboard.Colors.Background")!!) == null){
            presentHunt.getConfig().set("Leaderboard.Colors.Background", "49, 49, 49")
            presentHunt.save()
        }
        return Colors.getJavaColorFromString(presentHunt.getConfig().getString("Leaderboard.Colors.Background")!!)!!
    }

    /**
     * Returns the color of the leaderboard outline
     */
    private fun getLeaderboardOutlineColor(): Color {
        if(presentHunt.getConfig().getString("Leaderboard.Colors.Outline") == null
            || Colors.getJavaColorFromString(presentHunt.getConfig().getString("Leaderboard.Colors.Outline")!!) == null){
            presentHunt.getConfig().set("Leaderboard.Colors.Outline", "60, 60, 60")
            presentHunt.save()
        }
        return Colors.getJavaColorFromString(presentHunt.getConfig().getString("Leaderboard.Colors.Outline")!!)!!
    }

    /**
     * Returns the color of the leaderboard title
     */
    private fun getLeaderboardTitleColor(): Color {
        if(presentHunt.getConfig().getString("Leaderboard.Colors.Title") == null
            || Colors.getJavaColorFromString(presentHunt.getConfig().getString("Leaderboard.Colors.Title")!!) == null){
            presentHunt.getConfig().set("Leaderboard.Colors.Title", "153, 153, 153")
            presentHunt.save()
        }
        return Colors.getJavaColorFromString(presentHunt.getConfig().getString("Leaderboard.Colors.Title")!!)!!
    }

    /**
     * Returns the color of the leaderboard position
     */
    private fun getLeaderboardPositionColor(): Color {
        if(presentHunt.getConfig().getString("Leaderboard.Colors.PositionRidecount") == null
            || Colors.getJavaColorFromString(presentHunt.getConfig().getString("Leaderboard.Colors.PositionRidecount")!!) == null){
            presentHunt.getConfig().set("Leaderboard.Colors.PositionRidecount", "180, 180, 180")
            presentHunt.save()
        }
        return Colors.getJavaColorFromString(presentHunt.getConfig().getString("Leaderboard.Colors.PositionRidecount")!!)!!
    }

    /**
     * Returns the color of the leaderboard player name
     */
    private fun getLeaderboardNameColor(): Color {
        if(presentHunt.getConfig().getString("Leaderboard.Colors.Name") == null
            || Colors.getJavaColorFromString(presentHunt.getConfig().getString("Leaderboard.Colors.Name")!!) == null){
            presentHunt.getConfig().set("Leaderboard.Colors.Name", "250, 250, 250")
            presentHunt.save()
        }
        return Colors.getJavaColorFromString(presentHunt.getConfig().getString("Leaderboard.Colors.Name")!!)!!
    }

    /**
     * Returns the color of the leaderboard line
     */
    private fun getLeaderboardLineColor(): Color {
        if(presentHunt.getConfig().getString("Leaderboard.Colors.Line") == null
            || Colors.getJavaColorFromString(presentHunt.getConfig().getString("Leaderboard.Colors.Line")!!) == null){
            presentHunt.getConfig().set("Leaderboard.Colors.Line", "210, 210, 210")
            presentHunt.save()
        }
        return Colors.getJavaColorFromString(presentHunt.getConfig().getString("Leaderboard.Colors.Line")!!)!!
    }

}