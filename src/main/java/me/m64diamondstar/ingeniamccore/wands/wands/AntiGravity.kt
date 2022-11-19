package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import java.lang.Runnable
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.EulerAngle
import org.bukkit.util.Vector
import java.util.*

class AntiGravity: Wand {
    private val stands: MutableList<ArmorStand> = ArrayList()

    override fun getDisplayName(): String{
        return Colors.format("#bd5ebc&lA#b85abc&ln#b256bd&lt#ad53bd&li" +
                " #a74fbe&lG#a24bbe&lr#9c47bf&la#9744bf&lv#9240bf&li#8c3cc0&lt#8738c0&ly" +
                " #8134c1&lW#7c31c1&la#762dc2&ln#7129c2&ld")
    }

    override fun hasPermission(player: Player): Boolean {
        return player.hasPermission("ingeniawands.antigravity")
    }

    override fun getCustomModelData(): Int {
        return 16
    }

    override fun run(player: Player) {
        player.setGravity(false)
        player.velocity = Vector(player.velocity.x, 0.2, player.velocity.z)
        for (i in 0..17) {
            val loc = player.location.add(0.0, -0.5, 0.0)
            loc.yaw = (i * 20).toFloat()
            val armorStand = player.world.spawnEntity(loc, EntityType.ARMOR_STAND) as ArmorStand
            armorStand.isVisible = false
            armorStand.setGravity(false)
            armorStand.rightArmPose = EulerAngle(Math.toRadians(0.0), Math.toRadians(90.0), Math.toRadians(90.0))
            Objects.requireNonNull(armorStand.equipment)?.setItemInMainHand(ItemStack(Material.PINK_STAINED_GLASS))
            stands.add(armorStand)
        }
        val s = Bukkit.getScheduler().scheduleSyncRepeatingTask(
            IngeniaMC.plugin, {
                for (stand in stands) {
                    val loc = player.location.add(0.0, -0.5, 0.0)
                    loc.yaw = stand.location.yaw + 10
                    stand.teleport(loc)
                }
                player.world.spawnParticle(Particle.CRIMSON_SPORE, player.location, 20, 0.3, 0.0, 0.3, 0.0)
            }, 0L, 1L
        )
        Bukkit.getScheduler().runTaskLater(
            IngeniaMC.plugin, Runnable {
                player.setGravity(true)
                player.addPotionEffect(PotionEffectType.SLOW_FALLING.createEffect(50, 1))
                Bukkit.getScheduler().cancelTask(s)
                for (stand in stands) {
                    stand.remove()
                }
            }, 100L
        )
        Cooldowns.addPlayer(player, 7500L, 8000L, 9500L, 12000L)
    }
}