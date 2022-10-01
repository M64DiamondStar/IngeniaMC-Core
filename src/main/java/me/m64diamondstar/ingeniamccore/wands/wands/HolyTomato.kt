package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.Main
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class HolyTomato(player: Player) {
    init {
        val loc = player.location.add(0.0, 1.0, 0.0)
        val item = ItemStack(Material.BAKED_POTATO)
        val meta = item.itemMeta
        player.world.spawnParticle(Particle.REDSTONE, loc, 100, 1.0, 1.0, 1.0, 0.0, Particle.DustOptions(Color.RED, 1F))
        assert(meta != null)
        meta!!.setCustomModelData(1)
        item.itemMeta = meta
        val schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(
            Main.plugin, {
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
                Main.plugin, {
                    player.world.spawnParticle(Particle.CLOUD, ti.location, 1, 0.0, 0.0, 0.0, 0.0)
                    ti.remove()
                    Bukkit.getScheduler().cancelTask(schedule)
                }, 40L
            )
        }
        Cooldowns.addPlayer(player, 1000L, 3000L, 4000L, 6000L)
    }
}