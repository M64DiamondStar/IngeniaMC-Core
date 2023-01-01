package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class Fly: Wand {

    override fun getDisplayName(): String{
        return Colors.format("#78518c&lF#835c95&ll#8d679e&ly" +
                " #9872a7&lW#a27db0&la#ad88b9&ln#b793c2&ld")
    }

    override fun getCustomModelData(): Int {
        return 2
    }

    override fun hasPermission(player: Player): Boolean {
        return player.hasPermission("ingeniawands.fly")
    }

    override fun run(player: Player) {
        player.setGravity(false)

        object : BukkitRunnable() {
            var c = 0
            override fun run() {
                if(IngeniaPlayer(player).isInGame || c == 160){
                    player.setGravity(true)
                    this.cancel()
                    return
                }
                player.velocity = player.location.direction.multiply(0.5)
                player.world.spawnParticle(Particle.CLOUD, player.location, 3, 0.0, 0.0, 0.0, 0.0)

                c++
            }
        }.runTaskTimer(IngeniaMC.plugin, 0L, 1L)

        Cooldowns.addPlayer(player, 8000L, 9000L, 12000L, 15000L)
    }
}