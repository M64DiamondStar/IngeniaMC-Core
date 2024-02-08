package me.m64diamondstar.ingeniamccore.games.wandclash.clashwands.agility

import me.m64diamondstar.ingeniamccore.games.wandclash.util.ClashWand
import me.m64diamondstar.ingeniamccore.games.wandclash.util.ClashWandType
import org.bukkit.Particle
import org.bukkit.entity.Player
import kotlin.math.cos
import kotlin.math.sin

class Launch: ClashWand {
    override fun getID(): String {
        return "launch"
    }

    override fun getDisplayName(): String {
        return "Launch"
    }

    override fun getType(): ClashWandType {
        return ClashWandType.AGILITY
    }

    override fun getManaCost(): Int {
        return 30
    }

    override fun getCooldown(): Int {
        return 4000
    }

    override fun execute(player: Player): Boolean {
        // Launch player
        val direction = player.location.direction.normalize().multiply(1.5)
        direction.setY(0.6) // Sets the Y so players can't just launch themselves up
        player.velocity = direction

        // Cool visual effect
        for(i in 0..180) {
            for(r in 1..3) {
                val x = sin(Math.PI * i / 90.0) * r / 3.0
                val z = cos(Math.PI * i / 90.0) * r / 3.0

                val location = player.location.clone().add(x, 0.0, z)
                location.world.spawnParticle(Particle.END_ROD, location, 0, 0.0, 0.0, 0.0)
            }
        }
        return true
    }
}