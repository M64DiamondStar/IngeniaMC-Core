package me.m64diamondstar.ingeniamccore.wands

import java.util.HashMap
import org.bukkit.entity.Player

object Cooldowns {
    var cooldowns: MutableMap<String, Long> = HashMap()
    fun addPlayer(player: Player, op: Long, vipp: Long, vip: Long, visitor: Long) {
        if (player.hasPermission("ingeniawands.owner") || player.isOp) {
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
                player.sendMessage("§6§lI§e§lngenia §8§l> §cWait §4$timeleft §4Second(s) §cto use this wand again!")
                return true
            }
        }
        return false
    }
}