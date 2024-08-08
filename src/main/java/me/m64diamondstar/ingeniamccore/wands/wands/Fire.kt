package me.m64diamondstar.ingeniamccore.wands.wands

import io.papermc.paper.entity.TeleportFlag
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.wands.utils.Cooldowns
import me.m64diamondstar.ingeniamccore.wands.utils.Wand
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import kotlin.math.cos
import kotlin.math.sin

class Fire: Wand {

    override fun getDisplayName(): String{
        return Colors.format("#a80000&lF#a90d02&li#a91b03&lr#aa2805&le #ab3607&lW#ac4309&la#ac510a&ln#ad5e0c&ld")
    }

    override fun getCustomModelData(): Int {
        return 13
    }

    override fun hasPermission(player: Player): Boolean {
        return player.hasPermission("ingeniawands.fire")
    }

    override fun run(player: Player) {

        val nLoc = player.location

        val s = Bukkit.getScheduler().scheduleSyncRepeatingTask(
            IngeniaMC.plugin, {
                player.teleport(nLoc, TeleportFlag.EntityState.RETAIN_PASSENGERS)
            }, 0L, 1L
        )

        Bukkit.getScheduler().scheduleSyncDelayedTask(
            IngeniaMC.plugin, {
                val location1 = player.eyeLocation
                val particles = 50
                val radius = 0.7f
                for (i in 0 until particles) {
                    var x: Double
                    var z: Double
                    val angle: Double = 2 * Math.PI * i / particles
                    x = cos(angle) * radius
                    z = sin(angle) * radius
                    location1.add(x, 0.0, z)
                    player.world.spawnParticle(Particle.FLAME, location1, 1, 0.0, 0.0, 0.0, 0.0)
                    location1.subtract(x, 0.0, z)
                }
            }, 3L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            IngeniaMC.plugin, {
                val location2 = player.eyeLocation
                val particles = 50
                val radius = 0.7f
                for (i in 0 until particles) {
                    var x: Double
                    var z: Double
                    val angle: Double = 2 * Math.PI * i / particles
                    x = cos(angle) * radius
                    z = sin(angle) * radius
                    location2.add(x, -0.66, z)
                    player.world.spawnParticle(Particle.FLAME, location2, 1, 0.0, 0.0, 0.0, 0.0)
                    location2.subtract(x, -0.66, z)
                }
            }, 5L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            IngeniaMC.plugin, {
                val location3 = player.eyeLocation
                val particles = 50
                val radius = 0.7f
                for (i in 0 until particles) {
                    var x: Double
                    var z: Double
                    val angle: Double = 2 * Math.PI * i / particles
                    x = cos(angle) * radius
                    z = sin(angle) * radius
                    location3.add(x, -1.33, z)
                    player.world.spawnParticle(Particle.FLAME, location3, 1, 0.0, 0.0, 0.0, 0.0)
                    location3.subtract(x, -1.33, z)
                }
            }, 8L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            IngeniaMC.plugin, {
                Bukkit.getScheduler().cancelTask(s)
                val block = player.getTargetBlock(null, 21)
                player.world.spawnParticle(Particle.CLOUD, player.location, 150, 0.35, 0.7, 0.35, 0.0)
                player.world.spawnParticle(Particle.LAVA, player.location, 30, 0.2, 0.7, 0.2, 0.0)
                player.teleport(block.location.setDirection(player.location.direction), TeleportFlag.EntityState.RETAIN_PASSENGERS)
                player.teleport(player.location.add(player.location.direction.normalize().multiply(-1)), TeleportFlag.EntityState.RETAIN_PASSENGERS)
                player.teleport(player.location.add(0.5, 0.0, 0.5), TeleportFlag.EntityState.RETAIN_PASSENGERS)
                if (player.location.block.type != Material.AIR) player.teleport(player.location.add(0.0, 1.0, 0.0), TeleportFlag.EntityState.RETAIN_PASSENGERS)
                player.world.spawnParticle(
                    Particle.SMOKE,
                    player.location.add(0.0, 1.0, 0.0),
                    100,
                    0.3,
                    0.5,
                    0.3,
                    0.0
                )
                player.world.spawnParticle(Particle.LAVA, player.location.add(0.0, 1.0, 0.0), 30, 0.3, 0.5, 0.3, 0.0)
            }, 10L
        )
        Cooldowns.addPlayer(player, 0L, 3000L, 4000L, 6000L)
    }
}