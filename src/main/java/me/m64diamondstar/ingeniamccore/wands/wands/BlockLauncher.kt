package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.entity.Player

class BlockLauncher: Wand {

    override fun getDisplayName(): String{
        return Colors.format("#71c427&lB#6dbd25&ll#68b523&lo#64ae21&lc#60a71f&lk" +
                " #5b9f1c&lL#57981a&la#539118&lu#4f8a16&ln#4a8214&lc#467b12&lh#427410&le#3d6c0e&lr" +
                " #39650b&lW#355e09&la#305607&ln#2c4f05&ld")
    }

    override fun getCustomModelData(): Int {
        return 5
    }

    override fun hasPermission(player: Player): Boolean {
        return player.hasPermission("ingeniawands.blocklauncher")
    }

    override fun run(player: Player) {
        val loc = player.location.add(0.0, -1.0, 0.0)
        val block = loc.block
        val fallingblock = player.world.spawnFallingBlock(player.location.add(0.0, 1.0, 0.0), block.blockData)
        fallingblock.velocity = player.location.add(0.0, 1.0, 0.0).direction.multiply(1.5)
        fallingblock.customName = "FallingBlockWand"
        fallingblock.dropItem = false
        Cooldowns.addPlayer(player, 0L, 500L, 1500L, 3000L)
    }
}