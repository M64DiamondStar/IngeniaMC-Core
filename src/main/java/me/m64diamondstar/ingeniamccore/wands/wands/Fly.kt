package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.Main
import me.m64diamondstar.ingeniamccore.utils.Colors
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.entity.Player

class Fly(player: Player): Wand {
    private var player: Player

    init {
        this.player = player
    }

    override fun getDisplayName(): String{
        return Colors.format("#78518c&lF#835c95&ll#8d679e&ly" +
                " #9872a7&lW#a27db0&la#ad88b9&ln#b793c2&ld")
    }

    override fun getCustomModelData(): Int {
        return 2
    }

    override fun hasPermission(): Boolean {
        return player.hasPermission("ingeniawands.fly")
    }

    override fun run() {
        player.setGravity(false)
        val schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(
            Main.plugin, {
                player.velocity = player.location.direction.multiply(0.5)
                player.world.spawnParticle(Particle.CLOUD, player.location, 3, 0.0, 0.0, 0.0, 0.0)
            }, 0L, 1L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                Bukkit.getScheduler().cancelTask(schedule)
                player.setGravity(true)
            }, 160L
        )
        Cooldowns.addPlayer(player, 8000L, 9000L, 12000L, 15000L)
    }
}