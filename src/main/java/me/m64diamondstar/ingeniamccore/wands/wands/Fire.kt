package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.Main
import me.m64diamondstar.ingeniamccore.utils.Colors
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import kotlin.math.cos
import kotlin.math.sin

class Fire(player: Player): Wand {
    private var player: Player

    init {
        this.player = player
    }

    override fun getDisplayName(): String{
        return Colors.format("#a80000&lF#a90d02&li#a91b03&lr#aa2805&le #ab3607&lW#ac4309&la#ac510a&ln#ad5e0c&ld")
    }

    override fun getCustomModelData(): Int {
        return 13
    }

    override fun hasPermission(): Boolean {
        return player.hasPermission("ingeniawands.fire")
    }

    override fun run() {

        val nLoc = player.location

        val s = Bukkit.getScheduler().scheduleSyncRepeatingTask(
            Main.plugin, {
                player.teleport(nLoc)
            }, 0L, 1L
        )

        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                val location1 = player.eyeLocation
                val particles = 50
                val radius = 0.7f
                for (i in 0 until particles) {
                    var x: Double
                    var z: Double
                    val angle: Double = 2 * Math.PI * i / particles
                    x = Math.cos(angle) * radius
                    z = Math.sin(angle) * radius
                    location1.add(x, 0.0, z)
                    player.world.spawnParticle(Particle.FLAME, location1, 1, 0.0, 0.0, 0.0, 0.0)
                    location1.subtract(x, 0.0, z)
                }
            }, 3L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                val location2 = player.eyeLocation
                val particles = 50
                val radius = 0.7f
                for (i in 0 until particles) {
                    var x: Double
                    var z: Double
                    val angle: Double = 2 * Math.PI * i / particles
                    x = Math.cos(angle) * radius
                    z = Math.sin(angle) * radius
                    location2.add(x, -0.66, z)
                    player.world.spawnParticle(Particle.FLAME, location2, 1, 0.0, 0.0, 0.0, 0.0)
                    location2.subtract(x, -0.66, z)
                }
            }, 5L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                val location3 = player.eyeLocation
                val particles = 50
                val radius = 0.7f
                for (i in 0 until particles) {
                    var x: Double
                    var z: Double
                    val angle: Double = 2 * Math.PI * i / particles
                    x = Math.cos(angle) * radius
                    z = Math.sin(angle) * radius
                    location3.add(x, -1.33, z)
                    player.world.spawnParticle(Particle.FLAME, location3, 1, 0.0, 0.0, 0.0, 0.0)
                    location3.subtract(x, -1.33, z)
                }
            }, 8L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                Bukkit.getScheduler().cancelTask(s)
                val block = player.getTargetBlock(null, 21)
                player.world.spawnParticle(Particle.CLOUD, player.location, 150, 0.35, 0.7, 0.35, 0.0)
                player.world.spawnParticle(Particle.LAVA, player.location, 30, 0.2, 0.7, 0.2, 0.0)
                player.teleport(block.location.setDirection(player.location.direction))
                player.teleport(player.location.add(player.location.direction.normalize().multiply(-1)))
                player.teleport(player.location.add(0.5, 0.0, 0.5))
                if (player.location.block.type != Material.AIR) player.teleport(player.location.add(0.0, 1.0, 0.0))
                player.world.spawnParticle(
                    Particle.SMOKE_NORMAL,
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