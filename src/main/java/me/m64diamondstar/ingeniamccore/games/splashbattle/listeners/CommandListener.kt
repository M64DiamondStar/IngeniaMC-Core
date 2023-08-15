package me.m64diamondstar.ingeniamccore.games.splashbattle.listeners

import me.m64diamondstar.ingeniamccore.games.splashbattle.SplashBattleUtils
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class CommandListener: Listener {

    @EventHandler
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        val cmd = e.message.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        if(!SplashBattleUtils.players.contains(e.player)) return
        if (!SplashBattleUtils.playerCombat.containsKey(e.player)) return
        if (!cmd.equals("/leave", ignoreCase = true) && !cmd.equals("/msg", ignoreCase = true) &&
            !cmd.equals("/r", ignoreCase = true)
        ) {
            e.isCancelled = true
            e.player.sendMessage(Colors.format(MessageType.ERROR + "The commands you can use are limited."))
        }
        if (e.message.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0].equals("/leave", ignoreCase = true)) {
            if (System.currentTimeMillis() - SplashBattleUtils.playerCombat[e.player]!! > 5000) return
            e.player.sendMessage(Colors.format(MessageType.ERROR + "Please wait at least 5 seconds after hitting or getting hit."))
            e.isCancelled = true
        }
    }

}