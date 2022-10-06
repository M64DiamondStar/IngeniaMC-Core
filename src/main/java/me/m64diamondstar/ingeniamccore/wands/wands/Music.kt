package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.Main
import me.m64diamondstar.ingeniamccore.utils.Colors
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

class Music(player: Player): Wand {
    private var player: Player

    init {
        this.player = player
    }

    override fun getDisplayName(): String{
        return Colors.format("#9d0fff&lM#9310e9&lu#8910d3&ls#7f11bd&li#7512a7&lc #6a1291&lW#60137b&la#561365&ln#4c144f&ld")
    }

    override fun getCustomModelData(): Int {
        return 4
    }

    override fun hasPermission(): Boolean {
        return player.hasPermission("ingeniawands.music")
    }

    override fun run() {
        player.world.spawnParticle(Particle.NOTE, player.location, 50, 2.0, 1.0, 2.0)
        val disc = ItemStack(Material.MUSIC_DISC_CAT)
        val db = player.world.spawnEntity(player.location.add(0.0, 2.5, 0.0), EntityType.ARMOR_STAND) as ArmorStand
        db.isVisible = false
        Objects.requireNonNull(db.equipment)?.helmet = ItemStack(Material.RED_STAINED_GLASS)
        db.isSilent = true
        db.setGravity(false)
        val schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(
            Main.plugin, { db.teleport(player.location.add(0.0, 2.5, 0.0)) }, 0L, 1L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                db.equipment!!.helmet = ItemStack(Material.RED_STAINED_GLASS)
                db.world.spawnParticle(Particle.NOTE, db.location.add(0.0, -3.0, 0.0), 50, 2.0, 1.0, 2.0)
                val dropitem = player.world.dropItem(player.location.add(0.0, 2.0, 0.0), disc)
                dropitem.pickupDelay = Int.MAX_VALUE
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                    Main.plugin, { dropitem.remove() }, 20L
                )
            }, 10L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                db.equipment!!.helmet = ItemStack(Material.ORANGE_STAINED_GLASS)
                db.world.spawnParticle(Particle.NOTE, db.location.add(0.0, -3.0, 0.0), 50, 2.0, 1.0, 2.0)
                val dropitem = player.world.dropItem(player.location.add(0.0, 2.0, 0.0), disc)
                dropitem.pickupDelay = Int.MAX_VALUE
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                    Main.plugin, { dropitem.remove() }, 20L
                )
            }, 20L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                db.equipment!!.helmet = ItemStack(Material.YELLOW_STAINED_GLASS)
                db.world.spawnParticle(Particle.NOTE, db.location.add(0.0, -3.0, 0.0), 50, 2.0, 1.0, 2.0)
                val dropitem = player.world.dropItem(player.location.add(0.0, 2.0, 0.0), disc)
                dropitem.pickupDelay = Int.MAX_VALUE
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                    Main.plugin, { dropitem.remove() }, 20L
                )
            }, 30L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                db.equipment!!.helmet = ItemStack(Material.LIME_STAINED_GLASS)
                db.world.spawnParticle(Particle.NOTE, db.location.add(0.0, -3.0, 0.0), 50, 2.0, 1.0, 2.0)
                val dropitem = player.world.dropItem(player.location.add(0.0, 2.0, 0.0), disc)
                dropitem.pickupDelay = Int.MAX_VALUE
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                    Main.plugin, { dropitem.remove() }, 20L
                )
            }, 40L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                db.equipment!!.helmet = ItemStack(Material.GREEN_STAINED_GLASS)
                db.world.spawnParticle(Particle.NOTE, db.location.add(0.0, -3.0, 0.0), 50, 2.0, 1.0, 2.0)
                val dropitem = player.world.dropItem(player.location.add(0.0, 2.0, 0.0), disc)
                dropitem.pickupDelay = Int.MAX_VALUE
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                    Main.plugin, { dropitem.remove() }, 20L
                )
            }, 50L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                db.equipment!!.helmet = ItemStack(Material.BLUE_STAINED_GLASS)
                db.world.spawnParticle(Particle.NOTE, db.location.add(0.0, -3.0, 0.0), 50, 2.0, 1.0, 2.0)
                val dropitem = player.world.dropItem(player.location.add(0.0, 2.0, 0.0), disc)
                dropitem.pickupDelay = Int.MAX_VALUE
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                    Main.plugin, { dropitem.remove() }, 20L
                )
            }, 60L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                db.equipment!!.helmet = ItemStack(Material.LIGHT_BLUE_STAINED_GLASS)
                db.world.spawnParticle(Particle.NOTE, db.location.add(0.0, -3.0, 0.0), 50, 2.0, 1.0, 2.0)
                val dropitem = player.world.dropItem(player.location.add(0.0, 2.0, 0.0), disc)
                dropitem.pickupDelay = Int.MAX_VALUE
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                    Main.plugin, { dropitem.remove() }, 20L
                )
            }, 70L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                db.equipment!!.helmet = ItemStack(Material.PINK_STAINED_GLASS)
                db.world.spawnParticle(Particle.NOTE, db.location.add(0.0, -3.0, 0.0), 50, 2.0, 1.0, 2.0)
                val dropitem = player.world.dropItem(player.location.add(0.0, 2.0, 0.0), disc)
                dropitem.pickupDelay = Int.MAX_VALUE
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                    Main.plugin, { dropitem.remove() }, 20L
                )
            }, 80L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                db.equipment!!.helmet = ItemStack(Material.MAGENTA_STAINED_GLASS)
                db.world.spawnParticle(Particle.NOTE, db.location.add(0.0, -3.0, 0.0), 50, 2.0, 1.0, 2.0)
                val dropitem = player.world.dropItem(player.location.add(0.0, 2.0, 0.0), disc)
                dropitem.pickupDelay = Int.MAX_VALUE
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                    Main.plugin, { dropitem.remove() }, 20L
                )
            }, 90L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                db.equipment!!.helmet = ItemStack(Material.PURPLE_STAINED_GLASS)
                db.world.spawnParticle(Particle.NOTE, db.location.add(0.0, -3.0, 0.0), 50, 2.0, 1.0, 2.0)
                val dropitem = player.world.dropItem(player.location.add(0.0, 2.0, 0.0), disc)
                dropitem.pickupDelay = Int.MAX_VALUE
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                    Main.plugin, { dropitem.remove() }, 20L
                )
            }, 100L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                db.equipment!!.helmet = ItemStack(Material.LIGHT_BLUE_STAINED_GLASS)
                db.world.spawnParticle(Particle.NOTE, db.location.add(0.0, -3.0, 0.0), 50, 2.0, 1.0, 2.0)
                val dropitem = player.world.dropItem(player.location.add(0.0, 2.0, 0.0), disc)
                dropitem.pickupDelay = Int.MAX_VALUE
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                    Main.plugin, { dropitem.remove() }, 20L
                )
            }, 110L
        )
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            Main.plugin, {
                db.remove()
                db.world.spawnParticle(Particle.CLOUD, db.location.add(0.0, 1.5, 0.0), 10, 0.0, 0.0, 0.0, 0.0)
                Bukkit.getScheduler().cancelTask(schedule)
            }, 120L
        )
        Cooldowns.addPlayer(player, 6000L, 7000L, 8000L, 10000L)
    }
}