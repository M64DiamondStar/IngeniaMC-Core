package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.scheduler.BukkitRunnable

class SnowCannon(player: Player): Wand {
    private var player: Player

    init {
        this.player = player
    }

    override fun getDisplayName(): String{
        return Colors.format("#4f4f4f&lS#686868&ln#818181&lo#9a9a9a&lw" +
                " #b4b4b4&lC#cdcdcd&la#e6e6e6&ln#ffffff&ln#ffffff&lo#dcdcdc&ln #b9b9b9&lW#959595&la#727272&ln#4f4f4f&ld")
    }

    override fun getCustomModelData(): Int {
        return 3
    }

    override fun hasPermission(): Boolean {
        return player.hasPermission("ingeniawands.snowcannon")
    }

    override fun run() {
        val snowball = player.launchProjectile(Snowball::class.java, player.location.direction)
        object : BukkitRunnable() {
            override fun run() {
                player.world.spawnParticle(
                    Particle.REDSTONE, snowball.location, 2, 0.0, 0.0, 0.0, 0.0, Particle.DustOptions(
                        Color.WHITE, 1F
                    )
                )
                if (snowball.isDead) cancel()
            }
        }.runTaskTimer(IngeniaMC.plugin, 0L, 1L)
    }
}