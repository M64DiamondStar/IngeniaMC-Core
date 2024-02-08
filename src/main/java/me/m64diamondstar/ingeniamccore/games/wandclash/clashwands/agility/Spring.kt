package me.m64diamondstar.ingeniamccore.games.wandclash.clashwands.agility

import me.m64diamondstar.ingeniamccore.games.wandclash.util.ClashWand
import me.m64diamondstar.ingeniamccore.games.wandclash.util.ClashWandType
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.Particle
import org.bukkit.entity.Player
import kotlin.math.cos
import kotlin.math.sin

class Spring: ClashWand {
    override fun getID(): String {
        return "spring"
    }

    override fun getDisplayName(): String {
        return "Spring"
    }

    override fun getType(): ClashWandType {
        return ClashWandType.AGILITY
    }

    override fun getManaCost(): Int {
        return 20
    }

    override fun getCooldown(): Int {
        return 500
    }

    override fun execute(player: Player): Boolean {
        for(blockY in 1..5){
            if(!player.location.clone().add(0.0, (-blockY).toDouble(), 0.0).block.isEmpty){ // Check if the player is close to the ground

                // Launch player
                val direction = player.location.direction.normalize().multiply(0.35)
                direction.setY(1.6) // Sets the Y so players can't just launch themselves up
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

        // The player is not close to the ground
        player.sendMessage(Colors.format(MessageType.ERROR + "You need to be closer to the ground!"))
        return false
    }
}