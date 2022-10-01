package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.Main
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player

class Fire(player: Player) {
    init {
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                val location1 = player.eyeLocation
                val particles = 50
                val radius = 0.7f
                for (i in 0 until particles) {
                    var angle: Double
                    var x: Double
                    var z: Double
                    angle = 2 * Math.PI * i / particles
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
                    var angle: Double
                    var x: Double
                    var z: Double
                    angle = 2 * Math.PI * i / particles
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
                    var angle: Double
                    var x: Double
                    var z: Double
                    angle = 2 * Math.PI * i / particles
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