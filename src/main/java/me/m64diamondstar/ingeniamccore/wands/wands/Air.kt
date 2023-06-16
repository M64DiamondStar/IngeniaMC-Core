package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.wands.wandlistener.WandListener
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class Air: Wand {

    override fun getDisplayName(): String{
        return Colors.format("#ffffff&lA#d8e9f5&li#b1d4eb&lr #8abee1&lW#63a8d6&la#3c93cc&ln#157dc2&ld")
    }

    override fun getCustomModelData(): Int {
        return 12
    }

    override fun hasPermission(player: Player): Boolean {
        return player.hasPermission("ingeniawands.air")
    }

    override fun run(player: Player) {
        player.velocity = Vector(0, 1, 0)
        player.world.spawnParticle(Particle.SPELL, player.location, 100, 0.5, 0.5, 0.5, 0.01)
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            IngeniaMC.plugin, {
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
                if (c == 300 || player.isOnGround || IngeniaPlayer(player).isInGame) {
                    WandListener.gliding.remove(player)
                    cancel()
                    return
                }
                c++
            }
        }.runTaskTimer(IngeniaMC.plugin, 16L, 1L)
        Cooldowns.addPlayer(player, 15000L, 15000L, 18000L, 21000L)
    }
}