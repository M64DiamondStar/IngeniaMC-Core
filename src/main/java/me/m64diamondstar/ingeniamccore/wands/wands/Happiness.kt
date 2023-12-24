package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.wands.utils.Cooldowns
import me.m64diamondstar.ingeniamccore.wands.utils.Wand
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class Happiness: Wand {

    override fun getDisplayName(): String{
        return Colors.format("#dde810&lH#d4e110&la#ccda11&lp#c3d311&lp#bbcc11&li#b2c511&ln#aabe12&le#a1b712&ls#98b012&ls" +
                " #90a912&lW#87a213&la#7f9b13&ln#769413&ld")
    }

    override fun getCustomModelData(): Int {
        return 7
    }

    override fun hasPermission(player: Player): Boolean {
        return player.hasPermission("ingeniawands.happiness")
    }

    override fun run(player: Player) {
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
                }.runTaskTimer(IngeniaMC.plugin, 0, 3)
            }
        }
        Cooldowns.addPlayer(player, 0L, 5000L, 6000L, 8000L)
    }
}