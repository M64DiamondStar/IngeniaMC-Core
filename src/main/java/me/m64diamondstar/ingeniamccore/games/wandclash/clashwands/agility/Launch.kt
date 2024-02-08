package me.m64diamondstar.ingeniamccore.games.wandclash.clashwands.agility

import me.m64diamondstar.ingeniamccore.games.wandclash.util.ClashWand
import me.m64diamondstar.ingeniamccore.games.wandclash.util.ClashWandType
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
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

        val effectLocation = player.location.clone()
        effectLocation.y += 1.75
        effectLocation.pitch = 25f
        effectLocation.yaw += 180

        // Cool visual effect
        for(i in 0..180) {
            for(r in 1..3) {
                val x = sin(Math.PI * i / 90.0) * r / 3.0
                val y = cos(Math.PI * i / 90.0) * r / 3.0

                val location = LocationUtils.getRelativeLocation(effectLocation, r / 2.0 + 0.75, x, y)
                location.world.spawnParticle(Particle.END_ROD, location, 0, 0.0, 0.0, 0.0)
            }
        }
        return true
    }
}