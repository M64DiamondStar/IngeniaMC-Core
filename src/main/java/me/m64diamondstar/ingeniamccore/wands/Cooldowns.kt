package me.m64diamondstar.ingeniamccore.wands

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.MessageLocation
import java.util.HashMap
import org.bukkit.entity.Player
import kotlin.time.Duration.Companion.seconds

object Cooldowns {
    var cooldowns: MutableMap<String, Long> = HashMap()
    fun addPlayer(player: Player, op: Long, vipp: Long, vip: Long, visitor: Long) {
        if (player.isOp || player.hasPermission("ingenia.team")) {
            cooldowns[player.name] = System.currentTimeMillis() + op
        } else if (player.hasPermission("ingenia.vip+")) {
            cooldowns[player.name] = System.currentTimeMillis() + vipp
        } else if (player.hasPermission("ingenia.vip")) {
            cooldowns[player.name] = System.currentTimeMillis() + vip
        } else {
            cooldowns[player.name] = System.currentTimeMillis() + visitor
        }
    }

    @JvmStatic
    fun isOnCooldown(player: Player): Boolean {
        if (cooldowns.containsKey(player.name)) {
            if (cooldowns[player.name]!! > System.currentTimeMillis()) {
                val timeleft = (cooldowns[player.name]!! - System.currentTimeMillis()) / 1000
                val ingeniaPlayer = IngeniaPlayer(player)
                ingeniaPlayer.sendMessage("&cThis wand is on cooldown for ${timeleft.seconds}.", MessageLocation.HOTBAR)
                return true
            }
        }
        return false
    }
}