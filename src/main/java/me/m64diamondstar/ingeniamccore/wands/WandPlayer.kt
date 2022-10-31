package me.m64diamondstar.ingeniamccore.wands

import java.util.HashMap
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.entity.Player

class WandPlayer(private val player: Player) {
    var cooldown: Long = 0
        private set
    private var flying = false
    fun setCooldown(op: Long, vipp: Long, vip: Long, visitor: Long) {
        cooldown = if (player.hasPermission("ingeniawands.owner") || player.isOp) {
            System.currentTimeMillis() + op
        } else if (player.hasPermission("ingenia.vip+")) {
            System.currentTimeMillis() + vipp
        } else if (player.hasPermission("ingenia.vip")) {
            System.currentTimeMillis() + vip
        } else {
            System.currentTimeMillis() + visitor
        }
    }

    fun setFlying() {
        flying = true
    }

    fun setFlying(flying: Boolean) {
        this.flying = flying
    }
}