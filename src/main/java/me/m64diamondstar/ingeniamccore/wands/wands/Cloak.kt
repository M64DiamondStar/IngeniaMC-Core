package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.Main
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import java.util.HashMap
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Cloak(player: Player) {
    private var c = 0
    private val armorInv: MutableMap<Player, Array<ItemStack>> = HashMap()

    init {
        val loc = player.eyeLocation
        val nLoc = player.location
        val particles = 10
        val radius = 0.7f
        player.walkSpeed = 0.0f
        player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 20, 200))
        player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 20, 1))
        loc.add(0.0, -1.33, 0.0)
        player.teleport(nLoc)
        armorInv[player] = player.inventory.armorContents
        val s = Bukkit.getScheduler().scheduleSyncRepeatingTask(
            Main.plugin, {
                val angle: Double
                val x: Double
                val z: Double
                angle = 2 * Math.PI * c / particles
                x = Math.cos(angle) * radius
                z = Math.sin(angle) * radius
                loc.add(x, 0.1, z)
                player.world.spawnParticle(Particle.GLOW, loc, 10, 0.01, 0.01, 0.01, 0.0)
                loc.subtract(x, 0.0, z)
                player.teleport(nLoc)
                c++
            }, 0L, 1L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                Bukkit.getScheduler().cancelTask(s)
                player.world.spawnParticle(
                    Particle.SMOKE_LARGE,
                    player.location.add(0.0, 1.0, 0.0),
                    50,
                    0.3,
                    1.0,
                    0.3,
                    0.0
                )
                player.walkSpeed = 0.6f
                player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(100, 1))
                player.inventory.helmet = null
                player.inventory.chestplate = null
                player.inventory.leggings = null
                player.inventory.boots = null
            }, 20L
        )
        val s2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(
            Main.plugin, { player.inventory.heldItemSlot = 8 }, 0L, 1L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                player.world.spawnParticle(
                    Particle.SMOKE_LARGE,
                    player.location.add(0.0, 1.0, 0.0),
                    50,
                    0.3,
                    1.0,
                    0.3,
                    0.0
                )
                player.walkSpeed = 0.2f
                Bukkit.getScheduler().cancelTask(s2)
                player.inventory.heldItemSlot = 5
                player.inventory.armorContents = armorInv[player]
            }, 120L
        )
        Cooldowns.addPlayer(player, 6000L, 7000L, 9000L, 12000L)
    }
}