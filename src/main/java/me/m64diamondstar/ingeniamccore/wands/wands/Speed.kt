package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.Player

class Speed: Wand {

    override fun getDisplayName(): String{
        return Colors.format("#197859&lS#1e806a&lp#24887b&le#298f8c&le#2e979d&ld #339fad&lW#39a7be&la#3eaecf&ln#43b6e0&ld")
    }

    override fun getCustomModelData(): Int {
        return 15
    }

    override fun hasPermission(player: Player): Boolean {
        return player.hasPermission("ingeniawands.speed")
    }

    override fun run(player: Player) {
        player.walkSpeed = 0.5f
        val s = Bukkit.getScheduler().scheduleSyncRepeatingTask(
            IngeniaMC.plugin, {
                val dustTransition =
                    Particle.DustTransition(Color.fromRGB(60, 153, 176), Color.fromRGB(56, 205, 255), 1.5f)
                player.world.spawnParticle(
                    Particle.DUST_COLOR_TRANSITION,
                    player.location.add(0.0, 1.0, 0.0),
                    30,
                    0.2,
                    0.5,
                    0.2,
                    0.0,
                    dustTransition
                )
            }, 0L, 1L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            IngeniaMC.plugin, {
                player.walkSpeed = 0.2f
                Bukkit.getScheduler().cancelTask(s)
            }, 100L
        )
        Cooldowns.addPlayer(player, 5000L, 5000L, 6000L, 8000L)
    }
}