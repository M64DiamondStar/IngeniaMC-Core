package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.Main
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.scheduler.BukkitRunnable

class SnowCannon(player: Player) {
    init {
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
        }.runTaskTimer(Main.plugin, 0L, 1L)
    }
}