package me.m64diamondstar.ingeniamccore.games.wandclash.clashwands.agility

import me.m64diamondstar.ingeniamccore.games.wandclash.util.ClashWand
import me.m64diamondstar.ingeniamccore.games.wandclash.util.ClashWandType
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import org.bukkit.Particle
import org.bukkit.entity.Player
import kotlin.math.cos
import kotlin.math.sin

class BackStep: ClashWand {
    override fun getID(): String {
        return "back_step"
    }

    override fun getDisplayName(): String {
        return "Back Step"
    }

    override fun getType(): ClashWandType {
        return ClashWandType.AGILITY
    }

    override fun getManaCost(): Int {
        return 0
    }

    override fun getCooldown(): Int {
        return 5000
    }

    override fun execute(player: Player) {
        // Launch player backwards
        val direction = player.location.direction.normalize().multiply(-3)
        direction.setY(0.25) // Sets the Y so players can't just launch themselves up
        player.velocity = direction

        // Cool visual effect
        for(i in 0..180) {
            for(r in 1..3) {
                val x = sin(Math.PI * i / 90.0) * r / 3.0
                val y = cos(Math.PI * i / 90.0) * r / 3.0

                val location = LocationUtils.getRelativeLocation(player, 1.0, x, y)
                location.world.spawnParticle(Particle.END_ROD, location, 0, 0.0, 0.0, 0.0)
            }
        }
    }
}