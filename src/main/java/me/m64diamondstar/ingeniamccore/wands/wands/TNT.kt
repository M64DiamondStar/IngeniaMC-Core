package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Material
import org.bukkit.entity.Player

class TNT(player: Player) {
    init {
        val fb = player.world.spawnFallingBlock(player.location.add(0.0, 1.0, 0.0), Material.TNT.createBlockData())
        fb.velocity = player.location.direction.multiply(1.5)
        fb.dropItem = false
        fb.customName = "TnTWand"
        Cooldowns.addPlayer(player, 0L, 1000L, 3000L, 8000L)
    }
}