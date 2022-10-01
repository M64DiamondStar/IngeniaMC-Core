package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.Main
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.Player

class Speed(player: Player) {
    init {
        player.walkSpeed = 0.5f
        val s = Bukkit.getScheduler().scheduleSyncRepeatingTask(
            Main.plugin, {
                val dustTransition =
                    Particle.DustTransition(Color.fromRGB(60, 153, 176), Color.fromRGB(56, 205, 255), 1.5f)
                player.world.spawnParticle(
                    Particle.DUST_COLOR_TRANSITION,
                    player.location.add(0.0, 1.0, 0.0),
                    30,
                    0.2,
                    0.5,
                    0.2,
                    0.0,
                    dustTransition
                )
            }, 0L, 1L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                player.walkSpeed = 0.2f
                Bukkit.getScheduler().cancelTask(s)
            }, 100L
        )
        Cooldowns.addPlayer(player, 5000L, 5000L, 6000L, 8000L)
    }
}