package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.EulerAngle
import org.bukkit.util.Vector
import java.util.*

class Earth: Wand {

    override fun getDisplayName(): String{
        return Colors.format("#784108&lE#854d0d&la#925912&lr#9f6417&lt#ac701c&lh #b97c20&lW#c68825&la#d3932a&ln#e09f2f&ld")
    }

    override fun getCustomModelData(): Int {
        return 14
    }

    override fun hasPermission(player: Player): Boolean {
        return player.hasPermission("ingeniawands.earth")
    }

    override fun run(player: Player) {
        player.velocity = Vector(0, 1, 0)
        val walkspeed = player.walkSpeed
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            IngeniaMC.plugin, {
                player.velocity = Vector(0, -2, 0)
                player.walkSpeed = 0.8f
                player.fallDistance = 0f
            }, 15L
        )
        val schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(
            IngeniaMC.plugin, {
                val `as` =
                    player.world.spawnEntity(player.location.add(0.0, -1.6, 0.0), EntityType.ARMOR_STAND) as ArmorStand
                `as`.headPose = EulerAngle(
                    Math.toRadians(Math.random() * (15 + 15) - 15), Math.toRadians(Math.random() * (15 + 15) - 15),
                    Math.toRadians(Math.random() * (15 + 15) - 15)
                )
                `as`.setGravity(false)
                `as`.isVisible = false
                Objects.requireNonNull(`as`.equipment)?.helmet = ItemStack(player.location.add(0.0, -1.0, 0.0).block.type)
                `as`.world.spawnParticle(
                    Particle.BLOCK_CRACK, `as`.location.add(0.0, 2.2, 0.0), 30, 0.0, 0.0, 0.0, 0.0,
                    player.location.add(0.0, -1.0, 0.0).block.blockData
                )
                player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 10, 200))
                player.fallDistance = 0f
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                    IngeniaMC.plugin, { `as`.remove() }, 15L
                )
            }, 25L, 1L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            IngeniaMC.plugin, {
                Bukkit.getScheduler().cancelTask(schedule)
                player.walkSpeed = walkspeed
            }, 125
        )
        Cooldowns.addPlayer(player, 6500L, 8000L, 10000L, 14000L)
    }
}