package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Particle
import org.bukkit.entity.Player

class Launch(player: Player) {
    init {
        player.velocity = player.location.direction.multiply(2.5)
        player.world.spawnParticle(Particle.REVERSE_PORTAL, player.location, 300, 0.0, 0.0, 0.0)
        Cooldowns.addPlayer(player, 0L, 3000L, 4000L, 6000L)
    }
}