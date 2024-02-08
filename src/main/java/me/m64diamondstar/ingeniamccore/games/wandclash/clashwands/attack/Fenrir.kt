package me.m64diamondstar.ingeniamccore.games.wandclash.clashwands.attack

import me.m64diamondstar.ingeniamccore.games.wandclash.util.ClashWand
import me.m64diamondstar.ingeniamccore.games.wandclash.util.ClashWandType
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.util.Vector

/**
 * Sends Fenrir's Claws towards the enemy
 */
class Fenrir: ClashWand {

    override fun getID(): String {
        return "fenrir"
    }

    override fun getDisplayName(): String {
        return "Fenrir"
    }

    override fun getType(): ClashWandType {
        return ClashWandType.ATTACK
    }

    override fun getManaCost(): Int {
        return 40
    }

    override fun getCooldown(): Int {
        return 500
    }

    override fun execute(player: Player): Boolean {

        val playerLocation = player.eyeLocation.clone().add(0.0, -0.15, 0.0)

        for(pitcher in -5..5) {
            val pitchedLocation = playerLocation.clone()
            pitchedLocation.pitch += pitcher * 4
            for (i in -2..2) {
                val location = pitchedLocation.clone()
                location.yaw += i * 12.5f
                val add = location.direction.normalize().divide(Vector(2, 2, 2))

                for (j in 0..10) {
                    val options = Particle.DustOptions(Color.fromRGB(255, 255, 255), 0.35f)
                    player.world.spawnParticle<Particle.DustOptions>(Particle.REDSTONE, location.add(add), 1, 0.0, 0.0, 0.0, 1.0, options, true
                    )
                }
            }
        }
        return true
    }

}