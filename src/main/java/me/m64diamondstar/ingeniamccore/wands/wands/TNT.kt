package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.utils.Colors
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Material
import org.bukkit.entity.Player

class TNT(player: Player): Wand {
    private var player: Player

    init {
        this.player = player
    }

    override fun getDisplayName(): String{
        return Colors.format("#ff0000&lT#ff1414&lN#ff2828&lT #ff3c3c&lW#ff5050&la#ff6464&ln#ff7878&ld")
    }

    override fun getCustomModelData(): Int {
        return 8
    }

    override fun hasPermission(): Boolean {
        return player.hasPermission("ingeniawands.tnt")
    }

    override fun run() {
        val fb = player.world.spawnFallingBlock(player.location.add(0.0, 1.0, 0.0), Material.TNT.createBlockData())
        fb.velocity = player.location.direction.multiply(1.5)
        fb.dropItem = false
        fb.customName = "TnTWand"
        Cooldowns.addPlayer(player, 0L, 1000L, 3000L, 8000L)
    }
}