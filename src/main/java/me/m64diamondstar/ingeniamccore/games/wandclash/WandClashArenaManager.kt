package me.m64diamondstar.ingeniamccore.games.wandclash

import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.entity.Player

object WandClashArenaManager {

    fun join(player: Player, arena: String): Boolean{
        if(WandClashRegistry.existsArena(arena) && WandClashRegistry.getArena(arena)!!.getGamePhase().isJoinable()){

            // Check if the game already exists
            if(WandClashRegistry.existsGame(arena))
                WandClashRegistry.getGame(arena)!!.join(player)
            else{
                val game = WandClashGame(WandClashRegistry.getArena(arena)!!)

                // Register the game
                WandClashRegistry.registerGame(game)

                // Execute all other events which need to happen
                game.join(player)
            }

            return true
        }
        return false
    }

    fun leave(player: Player, arena: String): Boolean {
        if(WandClashRegistry.existsGame(arena)){
            if(WandClashRegistry.getGame(arena)!!.getPlayers().contains(player.uniqueId)) {
                WandClashRegistry.getGame(arena)!!.leave(player)
                player.sendMessage(Colors.format(MessageType.PLAYER_UPDATE + "You left the game of Wand Clash."))
                return true
            }
        }
        return false
    }

    fun leave(player: Player): Boolean {
        if(WandClashRegistry.isPlaying(player)){
            leave(player, WandClashRegistry.getPlayingGame(player)!!.arena.name)
            return true
        }
        return false
    }

}