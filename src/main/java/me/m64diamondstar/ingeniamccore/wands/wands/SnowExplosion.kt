package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Material
import org.bukkit.entity.Player

class SnowExplosion(player: Player) {
    init {
        val fb =
            player.world.spawnFallingBlock(player.location.add(0.0, 1.0, 0.0), Material.SNOW_BLOCK.createBlockData())
        fb.velocity = player.location.direction.multiply(1.5)
        fb.dropItem = false
        fb.customName = "SEWand"
        Cooldowns.addPlayer(player, 0L, 500L, 1500L, 4000L)
    }
}