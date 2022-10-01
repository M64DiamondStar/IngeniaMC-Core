package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.Main
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class Happiness(player: Player) {
    init {
        val maxDist = 5.0
        for (other in Bukkit.getOnlinePlayers()) {
            if (other.location.distance(player.location) <= maxDist) {
                object: BukkitRunnable(){

                    var c = 0

                    override fun run(){
                        val loc = other.location.add(0.0, 2.5, 0.0)
                        other.world.spawnParticle(Particle.HEART, loc, 2, 0.5, 0.5, 0.5, 0.0)
                        player.world.spawnParticle(Particle.VILLAGER_HAPPY, player.location.add(0.0, 2.0, 0.0), 5, 2.0, 2.0, 2.0, 0.0)

                        if(c == 33){
                            this.cancel()
                            return

                        }
                        c++
                    }
                }.runTaskTimer(Main.plugin, 0, 3)
            }
        }
        Cooldowns.addPlayer(player, 0L, 5000L, 6000L, 8000L)
    }
}