package me.m64diamondstar.ingeniamccore.wands.wands

import gg.flyte.twilight.scheduler.async
import gg.flyte.twilight.scheduler.repeat
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.wands.utils.Cooldowns
import me.m64diamondstar.ingeniamccore.wands.utils.Wand
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class FrostSpray: Wand {

    override fun getDisplayName(): String{
        return Colors.format("#0898FB&lF#139EFB&lr#1EA4FB&lo#29AAFB&ls#34B0FC&lt " +
                "#4ABCFC&lS#55C2FC&lp#60C9FC&lr#6BCFFC&la#76D5FC&ly " +
                "#8CE1FD&lW#97E7FD&la#A2EDFD&ln#ADF3FD&ld")
    }

    override fun getCustomModelData(): Int {
        return 21
    }

    override fun hasPermission(player: Player): Boolean {
        return player.hasPermission("ingeniawands.frostspray")
    }

    override fun run(player: Player) {
        var c = 0
        var booster = -12

        repeat(1) {
            kotlin.repeat(8) {
                async {
                    val spread = 0.3
                    val velocity = Vector(
                        (sin(c * 8 * PI / 360) + (Random.nextInt(0, 1000).toDouble() / 1000 * spread * 2 - spread)) * 0.4,
                        -0.8,
                        (cos(c * 8 * PI / 360) + (Random.nextInt(0, 1000).toDouble() / 1000 * spread * 2 - spread)) * 0.4
                    )

                    player.world.spawnParticle(Particle.SNOWFLAKE, player.location.clone().add(0.0, 1.0, 0.0), 0, velocity.x, velocity.y, velocity.z, 1.0, null, false)
                    player.world.spawnParticle(Particle.SNOWFLAKE, player.location.clone().add(0.0, 1.0, 0.0), 0, -velocity.x, velocity.y, -velocity.z, 1.0, null, false)
                }
            }

            val direction = player.location.clone().direction.normalize().multiply(0.03)

            if(getAirBlocksBelowPlayer(player) < 6 && c > 15){
                player.velocity = player.velocity.clone().setY(0).add(Vector(direction.x, 0.01 * booster, direction.z))
                booster++
            }else
                booster = -12

            when(c) {
                0 -> player.velocity = player.velocity.clone().setY(0).add(Vector(0.0, 1.0, 0.0))
                160 -> player.velocity = player.velocity.clone().setY(0).add(Vector(0.0, 1.0, 0.0))
            }


            if(c == 160){
                cancel()
            }

            c++
        }

        Cooldowns.addPlayer(player, 5000L, 6000L, 7500L, 10000L)
    }

    private fun getAirBlocksBelowPlayer(player: Player): Int {
        val location = player.location.clone() // Get player's location
        val world = location.world ?: return 0

        // Start from the block beneath the player's feet
        var currentY = location.blockY - 1
        var airCount = 0

        // Iterate downwards until reaching a solid block or the world bottom
        while (currentY >= world.minHeight) {
            val block = world.getBlockAt(location.blockX, currentY, location.blockZ)
            if (block.type != Material.AIR) {
                break
            }
            airCount++
            currentY--
        }

        return airCount
    }
}