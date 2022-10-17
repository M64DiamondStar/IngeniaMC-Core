package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Material
import org.bukkit.entity.Player

class SnowExplosion(player: Player): Wand {
    private var player: Player

    init {
        this.player = player
    }

    override fun getDisplayName(): String{
        return Colors.format("#ffffff&lS#fcf5f5&ln#f8ecec&lo#f5e2e2&lw" +
                " #f2d8d8&lE#eecece&lx#ebc5c5&lp#e7bbbb&ll#e4b1b1&lo#e1a7a7&ls#dd9e9e&li#da9494&lo#d78a8a&ln" +
                " #d38080&lW#d07777&la#cc6d6d&ln#c96363&ld")
    }

    override fun getCustomModelData(): Int {
        return 18
    }

    override fun hasPermission(): Boolean {
        return player.hasPermission("ingeniawands.snowexplosion")
    }

    override fun run() {
        val fb =
            player.world.spawnFallingBlock(player.location.add(0.0, 1.0, 0.0), Material.SNOW_BLOCK.createBlockData())
        fb.velocity = player.location.direction.multiply(1.5)
        fb.dropItem = false
        fb.customName = "SEWand"
        Cooldowns.addPlayer(player, 0L, 500L, 1500L, 4000L)
    }
}