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
import java.util.*

class Bouncer: Wand {
    private val stands1: MutableList<ArmorStand> = ArrayList()
    private val stands2: MutableList<ArmorStand> = ArrayList()
    private var y = 0

    override fun getDisplayName(): String{
        return Colors.format("#428017&lB#458722&lo#478d2c&lu#4a9437&ln#4d9a42&lc#50a14d&le#52a857&lr" +
                " #55ae62&lW#58b56d&la#5abb77&ln#5dc282&ld")
    }

    override fun getCustomModelData(): Int {
        return 17
    }

    override fun hasPermission(player: Player): Boolean {
        return player.hasPermission("ingeniawands.bouncer")
    }

    override fun run(player: Player) {
        player.addPotionEffect(PotionEffectType.JUMP.createEffect(200, 5))
        for (i in 0..1) {
            val loc = player.location.add(0.0, -0.5, 0.0)
            loc.yaw = (i * 180).toFloat()
            val armorStand = player.world.spawnEntity(loc, EntityType.ARMOR_STAND) as ArmorStand
            armorStand.isVisible = false
            armorStand.setGravity(false)
            armorStand.rightArmPose = EulerAngle(Math.toRadians(0.0), Math.toRadians(90.0), Math.toRadians(90.0))
            Objects.requireNonNull(armorStand.equipment)?.setItemInMainHand(ItemStack(Material.GREEN_STAINED_GLASS))
            stands1.add(armorStand)
        }
        for (i in 0..1) {
            val loc = player.location.add(0.0, -0.5, 0.0)
            loc.yaw = (i * 180 - 90).toFloat()
            val armorStand = player.world.spawnEntity(loc, EntityType.ARMOR_STAND) as ArmorStand
            armorStand.isVisible = false
            armorStand.setGravity(false)
            armorStand.rightArmPose = EulerAngle(Math.toRadians(0.0), Math.toRadians(90.0), Math.toRadians(90.0))
            Objects.requireNonNull(armorStand.equipment)?.setItemInMainHand(ItemStack(Material.LIME_STAINED_GLASS))
            stands2.add(armorStand)
        }
        val s = Bukkit.getScheduler().scheduleSyncRepeatingTask(
            IngeniaMC.plugin, {
                for (stand in stands1) {
                    val loc = player.location.add(
                        0.0, -0.5 +
                                Math.sin(y.toDouble() / 18 * Math.PI) / 2, 0.0
                    )
                    loc.yaw = stand.location.yaw + 10
                    stand.teleport(loc)
                }
                for (stand in stands2) {
                    val loc = player.location.add(
                        0.0, -0.5 +
                                Math.cos((y.toDouble() + 9) / 18 * Math.PI) / 2, 0.0
                    )
                    loc.yaw = stand.location.yaw + 10
                    stand.teleport(loc)
                }
                player.world.spawnParticle(
                    Particle.BLOCK_CRACK,
                    player.location,
                    20,
                    0.3,
                    0.0,
                    0.3,
                    0.0,
                    Material.SLIME_BLOCK.createBlockData()
                )
                y++
            }, 0L, 1L
        )
        Bukkit.getScheduler().runTaskLater(
            IngeniaMC.plugin, Runnable {
                Bukkit.getScheduler().cancelTask(s)
                for (stand in stands1) stand.remove()
                for (stand in stands2) stand.remove()
            }, 200L
        )
        Cooldowns.addPlayer(player, 10000L, 11000L, 13000L, 15000L)
    }
}