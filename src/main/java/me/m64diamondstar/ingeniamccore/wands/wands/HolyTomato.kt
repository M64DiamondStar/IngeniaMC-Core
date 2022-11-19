package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class HolyTomato: Wand {

    override fun getDisplayName(): String{
        return Colors.format("#630b0b&lH#710d0d&lo#7f0f0f&ll#8e1111&ly #9c1212&lT#aa1414&lo#b81616&lm#630b0b&la#710d0d&lt#7f0f0f&lo" +
                " #8e1111&lW#9c1212&la#aa1414&ln#b81616&ld")
    }

    override fun getCustomModelData(): Int {
        return 6
    }

    override fun hasPermission(player: Player): Boolean {
        return player.hasPermission("ingeniawands.holytomato")
    }

    override fun run(player: Player) {
        val loc = player.location.add(0.0, 1.0, 0.0)
        val item = ItemStack(Material.BAKED_POTATO)
        val meta = item.itemMeta
        player.world.spawnParticle(Particle.REDSTONE, loc, 100, 1.0, 1.0, 1.0, 0.0, Particle.DustOptions(Color.RED, 1F))
        assert(meta != null)
        meta!!.setCustomModelData(1)
        item.itemMeta = meta
        val schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(
            IngeniaMC.plugin, {
                player.world.spawnParticle(
                    Particle.REDSTONE, loc, 50, 1.0, 1.0, 1.0, 0.0, Particle.DustOptions(
                        Color.RED, 1F
                    )
                )
            }, 0L, 10L
        )
        for (i in 0..14) {
            val ti = player.world.dropItem(loc, item)
            ti.pickupDelay = Int.MAX_VALUE
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                IngeniaMC.plugin, {
                    player.world.spawnParticle(Particle.CLOUD, ti.location, 1, 0.0, 0.0, 0.0, 0.0)
                    ti.remove()
                    Bukkit.getScheduler().cancelTask(schedule)
                }, 40L
            )
        }
        Cooldowns.addPlayer(player, 1000L, 3000L, 4000L, 6000L)
    }
}