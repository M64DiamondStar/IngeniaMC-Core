package me.m64diamondstar.ingeniamccore.games.guesstheword

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import kotlin.random.Random

class GuessTheWordListener: Listener {

    @EventHandler
    fun onChatEvent(event: AsyncPlayerChatEvent){
        if(!GuessTheWordRegistry.isRunning()) return
        if(GuessTheWordRegistry.containsPlayer(event.player)) {
            var spoil = false
            GuessTheWordRegistry.getRunningWords().forEach{
                if(event.message.contains(it)) {
                    spoil = true
                }
            }
            if(spoil){
                event.isCancelled = true
                event.player.sendMessage(Colors.format("${MessageType.WARNING}Hey! Don't spoil it for others."))
            }
            return
        }
        if(!GuessTheWordRegistry.containsWord(event.message.lowercase())) return
        event.isCancelled = true

        val player = IngeniaPlayer(event.player)
        val exp = Random.nextLong(3, 8)
        player.addExp(exp)
        player.sendMessage(Colors.format("${MessageType.SUCCESS}Good guess, you got $exp exp from this game!"))
        GuessTheWordRegistry.addPlayer(event.player, event.message.lowercase())
    }


}