package me.m64diamondstar.ingeniamccore.games.wandclash

import me.m64diamondstar.ingeniamccore.games.wandclash.util.WandClashGamePhase
import me.m64diamondstar.ingeniamccore.games.wandclash.util.WandClashUtils
import org.bukkit.entity.Player
import java.util.UUID

object WandClashRegistry {

    private val arenas = ArrayList<WandClashArena>()
    private val games = ArrayList<WandClashGame>()
    private val players = HashMap<UUID, WandClashGame>()

    fun getArenas(): ArrayList<WandClashArena> {
        return arenas
    }

    fun registerArena(arena: WandClashArena){
        arenas.add(arena)
    }

    fun registerAllArenas(){
        for(file in WandClashUtils.getWandClashArenas()){
            registerArena(WandClashArena(file.name.replace(".yml", "")))
        }
    }

    fun existsArena(name: String): Boolean {
        return arenas.any { it.name == name }
    }

    fun getArena(name: String): WandClashArena? {
        return arenas.firstOrNull { it.name == name }
    }

    fun unregisterArena(arena: String) {
        arenas.removeIf { it.name == arena }
    }

    fun getGames(): ArrayList<WandClashGame> {
        return games
    }

    /**
     * Registers a game and puts it into the player waiting phase.
     */
    fun registerGame(game: WandClashGame){
        games.add(game)
        game.arena.setGamePhase(WandClashGamePhase.WAITING_FOR_PLAYERS)
    }

    fun unregisterGame(game: String) {
        games.removeIf { it.arena.name == game }
    }

    fun getGame(arena: String): WandClashGame? {
        return games.firstOrNull { it.arena.name == arena }
    }

    fun getGame(player: Player): WandClashGame? {
        return games.firstOrNull { it.getPlayers().contains(player.uniqueId) }
    }

    fun existsGame(arena: String): Boolean {
        return games.any { it.arena.name == arena }
    }

    fun isPlaying(player: Player): Boolean {
        return players.containsKey(player.uniqueId)
    }

    fun getPlayingGame(player: Player): WandClashGame? {
        return players[player.uniqueId]
    }

    fun setPlayingGame(player: Player, game: WandClashGame?){
        if(game == null){
            players.remove(player.uniqueId)
            return
        }
        players[player.uniqueId] = game
    }

}