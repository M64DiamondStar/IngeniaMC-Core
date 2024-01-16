package me.m64diamondstar.ingeniamccore.general.listeners.helpers

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.items.MaterialChecker
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFertilizeEvent
import org.bukkit.scheduler.BukkitRunnable


class BonemealListener: Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onBonemeal(event: BlockFertilizeEvent){
        val blocks = event.blocks

        object: BukkitRunnable(){

            override fun run() {
                blocks.forEach {

                    if (MaterialChecker(it.type).isFlower()) {
                        it.block.type = Material.SHORT_GRASS
                        val loc = it.location.add(0.5, 0.5, 0.5)

                        val dustOptions = DustOptions(Color.fromRGB(92, 161, 92), 0.8f)
                        loc.world?.spawnParticle(Particle.REDSTONE, loc, 50, 0.2, 0.2, 0.2, dustOptions)
                    }
                }
            }

        }.runTaskLater(IngeniaMC.plugin, 5L)

    }


}