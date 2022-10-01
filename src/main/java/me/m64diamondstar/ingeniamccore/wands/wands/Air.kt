package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.Main
import me.m64diamondstar.ingeniamccore.wands.wandlistener.WandListener
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class Air(player: Player) {
    init {
        player.velocity = Vector(0, 1, 0)
        player.world.spawnParticle(Particle.SPELL, player.location, 100, 0.5, 0.5, 0.5, 0.01)
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                player.velocity = player.location.direction.multiply(1.4)
                WandListener.gliding.add(player)
                player.isGliding = true
            }, 15L
        )
        object : BukkitRunnable() {
            var c = 0
            override fun run() {
                if (!(player as CraftPlayer).isOnGround) player.getWorld()
                    .spawnParticle(Particle.SPELL, player.getLocation(), 30, 0.1, 0.1, 0.1, 0.0)
                if (c == 300 || player.isOnGround) {
                    WandListener.gliding.remove(player)
                    cancel()
                    return
                }
                c++
            }
        }.runTaskTimer(Main.plugin, 16L, 1L)
        Cooldowns.addPlayer(player, 15000L, 15000L, 18000L, 21000L)
    }
}