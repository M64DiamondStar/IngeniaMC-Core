package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.entity.Player

class BlockLauncher(player: Player) {
    init {
        val loc = player.location.add(0.0, -1.0, 0.0)
        val block = loc.block
        val fallingblock = player.world.spawnFallingBlock(player.location.add(0.0, 1.0, 0.0), block.blockData)
        fallingblock.velocity = player.location.add(0.0, 1.0, 0.0).direction.multiply(1.5)
        fallingblock.customName = "FallingBlockWand"
        fallingblock.dropItem = false
        Cooldowns.addPlayer(player, 0L, 500L, 1500L, 3000L)
    }
}