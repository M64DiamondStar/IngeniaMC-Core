package me.m64diamondstar.ingeniamccore.wands.utils

import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

object Cooldowns {

    private var cooldowns: MutableMap<String, Long> = HashMap()

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
                val timeLeft = (cooldowns[player.name]!! - System.currentTimeMillis()) / 1000
                (player as Audience).sendActionBar(MiniMessage.miniMessage().deserialize("<${MessageType.ERROR}>Wand on cooldown for ${timeLeft.seconds.toLong(DurationUnit.SECONDS) + 1}s"))
                return true
            }
        }
        return false
    }
}