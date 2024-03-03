package me.m64diamondstar.ingeniamccore.games.wandclash

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.games.PhysicalGameType
import me.m64diamondstar.ingeniamccore.games.wandclash.util.WandClashGamePhase
import me.m64diamondstar.ingeniamccore.games.wandclash.util.WandClashTeam
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.warps.WarpUtils
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.UUID

class WandClashGame(val arena: WandClashArena) {

    private val players = ArrayList<UUID>()
    private val playerBoards = HashMap<UUID, WandClashGameBoard>()
    private val chosenTeam = HashMap<UUID, WandClashTeam>()
    private val playerInventories = HashMap<UUID, Array<ItemStack?>>()
    private var countdown = -1 // -1 means not enough players to start
    private var runnable: BukkitTask? = null

    fun join(player: Player): Boolean{
        if(arena.getGamePhase().isJoinable()) { //Check if the game is still join able, if not, return false
            if(countdown == -1 && arena.getData().minPlayers <= players.size + 1) { // If the countdown hasn't started and the minimum amount of players is reached
                countdown = arena.getData().playersCountdown // Start the countdown
            }
            if(players.isEmpty())
                startCountdown()

            IngeniaPlayer(player).game = PhysicalGameType.WAND_CLASH

            players.add(player.uniqueId)
            playerBoards[player.uniqueId] = WandClashGameBoard(player, this) // Add player board
            playerInventories[player.uniqueId] = player.inventory.contents
            player.inventory.contents = emptyArray()
            WandClashRegistry.setPlayingGame(player, this) // Register player to this game, so it can be found from other classes

            if(arena.getData().lobbySpawnLocation != null)
                player.teleport(arena.getData().lobbySpawnLocation!!)

            

            return true
        }
        return false
    }

    fun leave(player: Player): Boolean{
        if(players.contains(player.uniqueId)){

            IngeniaPlayer(player).game = null

            player.inventory.contents = playerInventories[player.uniqueId]

            if(playerBoards.containsKey(player.uniqueId))
                playerBoards[player.uniqueId]!!.removeBoard() // Remove the scoreboard

            players.remove(player.uniqueId)
            WandClashRegistry.setPlayingGame(player, null) // Remove the player from the game registry

            // Teleport player away from the game
            if(arena.getData().leaveLocation != null)
                player.teleport(arena.getData().leaveLocation!!)
            else
                player.teleport(WarpUtils.getNearestLocation(player))

            // Restart countdown if the game is still joinable and the minimum amount of players is not reached
            if (arena.getGamePhase().isPreGame()) {
                if (players.size < arena.getData().minPlayers) {
                    arena.setGamePhase(WandClashGamePhase.WAITING_FOR_PLAYERS)
                    countdown = -1
                    if(runnable != null)
                        runnable!!.cancel()
                }
            }

            return true
        }
        return false
    }

    /**
     * Handles all the countdown phases & the countdown board
     */
    private fun startCountdown() {
        arena.setGamePhase(WandClashGamePhase.WAITING_FOR_PLAYERS)
        runnable = object: BukkitRunnable() {
            override fun run() {
                if(countdown == -1) { // Not enough players to start
                    for(player in players){
                        playerBoards[player]!!.setCountdownBoard(-1)
                    }
                    return // Return so the code doesn't run any further
                }

                // Start next countdown or start game
                if (countdown == 0) {
                    when(arena.getGamePhase()){
                        WandClashGamePhase.WAITING_FOR_PLAYERS -> {
                            arena.setGamePhase(WandClashGamePhase.MODE_VOTE)
                            countdown = arena.getData().gameModeCountdown
                        }

                        WandClashGamePhase.MODE_VOTE -> {
                            // TODO Insert if statement to check if it's a team game mode
                            arena.setGamePhase(WandClashGamePhase.TEAM_CHOOSE)
                            countdown = arena.getData().teamCountdown
                        }

                        WandClashGamePhase.TEAM_CHOOSE -> {
                            startGame()
                        }

                        else -> {
                            this.cancel()
                            return
                        }
                    }
                    for (player in players) {
                        playerBoards[player]!!.setCountdownBoard(countdown)
                    }
                }

                // Count down
                else {
                    countdown--
                    for (player in players) {
                        playerBoards[player]!!.setCountdownBoard(countdown)
                    }
                }
            }
        }.runTaskTimerAsynchronously(IngeniaMC.plugin, 0, 20)
    }

    fun startGame(){
        arena.setGamePhase(WandClashGamePhase.ONGOING)
    }
    fun getPlayerCount(): Int = players.size
    fun getChosenTeam(player: Player): WandClashTeam? = chosenTeam[player.uniqueId]
    fun getPlayers(): ArrayList<UUID> = players

}