package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.Main
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

class Bush(player: Player) {
    init {
        val helmet = Objects.requireNonNull(player.equipment)?.helmet
        for (i in 0..64) {
            val as1 = player.world.spawnEntity(
                player.location.add(
                    Math.random() * (1.5 + 1.5) - 1.5,
                    Math.random() * (-0.3 + 1.6) - 2,
                    Math.random() * (1.5 + 1.5) - 1.5
                ), EntityType.ARMOR_STAND
            ) as ArmorStand
            Objects.requireNonNull(as1.equipment)?.helmet = ItemStack(Material.OAK_LEAVES)
            as1.isVisible = false
            as1.setGravity(false)
            as1.customName = "IngeniaWandsBush" + player.uniqueId
            player.equipment!!.helmet = ItemStack(Material.OAK_LEAVES)
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.plugin, {
                    as1.remove()
                    as1.world.spawnParticle(Particle.CLOUD, as1.location.add(0.0, 1.5, 0.0), 3, 0.0, 0.0, 0.0, 0.0)
                    if (player.equipment!!.helmet != null) {
                        player.equipment!!.helmet = helmet
                    } else player.equipment!!.helmet = ItemStack(Material.AIR)
                }, 100L
            )
        }
        Cooldowns.addPlayer(player, 0L, 10000L, 12000L, 15000L)
    }
}