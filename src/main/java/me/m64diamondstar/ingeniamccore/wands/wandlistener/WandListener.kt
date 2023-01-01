package me.m64diamondstar.ingeniamccore.wands.wandlistener

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.wands.Cooldowns.isOnCooldown
import me.m64diamondstar.ingeniamccore.wands.wands.*
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.entity.EntityToggleGlideEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*

class WandListener : Listener {
    @EventHandler
    fun onUseWand(e: PlayerInteractEvent) {
        val player = e.player
        val ingeniaPlayer = IngeniaPlayer(player)
        if (e.action == Action.RIGHT_CLICK_AIR || e.action == Action.RIGHT_CLICK_BLOCK) {
            if (player.inventory.itemInMainHand.type == Material.BLAZE_ROD &&
                player.inventory.itemInMainHand.hasItemMeta() &&
                player.inventory.itemInMainHand.itemMeta!!.hasCustomModelData() &&
                !isOnCooldown(player) &&
                !player.isInsideVehicle &&
                !ingeniaPlayer.isInGame
            ) {
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 1) Launch().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 2) Fly().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 3) SnowCannon().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 4) Music().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 5) BlockLauncher().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 6) HolyTomato().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 7) Happiness().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 8) TNT().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 9) Bush().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 10) Cloak().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 11) Water().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 12) Air().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 13) Fire().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 14) Earth().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 15) Speed().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 16) AntiGravity().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 17) Bouncer().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 18) SnowExplosion().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 19) Sled().run(player)
                if (player.inventory.itemInMainHand.itemMeta!!.customModelData == 20) Grapple().run(player)
            }
        }
    }

    @EventHandler
    fun onBlockChange(e: EntityChangeBlockEvent) {
        val entity = e.entity
        if (entity is FallingBlock) {
            if (entity.getCustomName() != null) {
                if (entity.getCustomName() == "FallingBlockWand") {
                    entity.getWorld().spawnParticle(Particle.LAVA, entity.getLocation(), 20)
                    e.isCancelled = true
                } else if (entity.getCustomName() == "TnTWand") {
                    entity.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, entity.getLocation(), 10)
                    val loc = entity.getLocation().add(0.0, -1.0, 0.0)
                    val block = loc.block
                    e.isCancelled = true
                    for (i in 0..5) {
                        val fb = entity.getWorld().spawnFallingBlock(entity.getLocation(), block.blockData)
                        fb.velocity = Vector(
                            Math.random() * (0.2 + 0.2) - 0.2,
                            Math.random() * (0.6 - 0.85) + 0.85,
                            Math.random() * (0.2 + 0.2) - 0.2
                        )
                        fb.dropItem = false
                        fb.customName = "TnTWandBlock"
                    }
                } else if (entity.getCustomName() == "SEWand") {
                    entity.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, entity.getLocation(), 10)
                    e.isCancelled = true
                    for (i in 0..19) {
                        val sb = entity.getWorld().spawn(entity.getLocation(), Snowball::class.java)
                        sb.velocity = Vector(
                            Math.random() * (0.2 + 0.2) - 0.2,
                            Math.random() * (0.6 - 0.85) + 0.85,
                            Math.random() * (0.2 + 0.2) - 0.2
                        )
                        object : BukkitRunnable() {
                            override fun run() {
                                entity.getWorld().spawnParticle(
                                    Particle.REDSTONE,
                                    sb.location, 2, 0.0, 0.0, 0.0, 0.0, Particle.DustOptions(Color.WHITE, 1f)
                                )
                                if (sb.isDead) cancel()
                            }
                        }.runTaskTimer(IngeniaMC.plugin, 0L, 1)
                    }
                } else if (entity.getCustomName() == "TnTWandBlock" || entity.getCustomName() == "igwandsearthwand") {
                    e.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    fun onPlayerToggleGlide(e: EntityToggleGlideEvent) {
        if (e.entity !is Player) return
        if (gliding.contains(e.entity)) e.isCancelled = true
    }

    companion object {
        var gliding = ArrayList<Player>()
    }
}