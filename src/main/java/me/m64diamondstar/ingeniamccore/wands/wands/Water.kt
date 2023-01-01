package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.wands.wandlistener.WandListener
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class Water: Wand {

    override fun getDisplayName(): String{
        return Colors.format("#1ca6ff&lW#1790e4&la#137aca&lt#0e63af&le#094d94&lr #1085ff&lW#107ade&la#1170bd&ln#11659c&ld")
    }

    override fun getCustomModelData(): Int {
        return 11
    }

    override fun hasPermission(player: Player): Boolean {
        return player.hasPermission("ingeniawands.water")
    }

    override fun run(player: Player) {
        properInit(player)
    }

    private fun properInit(player: Player){
        if (!(player as CraftPlayer).isOnGround) {
            player.sendMessage(Colors.format("&cYou can only use this wand on the ground!"))
            return
        }
        WandListener.gliding.add(player)
        val loc1 = player.getLocation().add(3.0, 0.0, 0.0)
        val loc2 = player.getLocation().add(0.0, 0.0, 3.0)
        val loc3 = player.getLocation().add(-3.0, 0.0, 0.0)
        val loc4 = player.getLocation().add(0.0, 0.0, -3.0)
        val walkspeed = player.getWalkSpeed()
        val flyspeed = player.getFlySpeed()
        player.setWalkSpeed(0f)
        player.setFlySpeed(0f)
        player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 50, 250, false))
        object : BukkitRunnable() {
            var c: Long = 0
            override fun run() {
                if (c == 40L) {
                    cancel()
                    return
                }
                player.getWorld().spawnParticle(Particle.FALLING_WATER, loc1, 40, 0.1, 0.1, 0.1, 0.0)
                player.getWorld().spawnParticle(Particle.FALLING_WATER, loc2, 40, 0.1, 0.1, 0.1, 0.0)
                player.getWorld().spawnParticle(Particle.FALLING_WATER, loc3, 40, 0.1, 0.1, 0.1, 0.0)
                player.getWorld().spawnParticle(Particle.FALLING_WATER, loc4, 40, 0.1, 0.1, 0.1, 0.0)
                loc1.add(0.0, 0.05, 0.0)
                loc2.add(0.0, 0.05, 0.0)
                loc3.add(0.0, 0.05, 0.0)
                loc4.add(0.0, 0.05, 0.0)
                c++
            }
        }.runTaskTimer(IngeniaMC.plugin, 0L, 1L)
        object : BukkitRunnable() {
            var c: Long = 0
            override fun run() {
                if (c == 10L) {
                    cancel()
                    return
                }
                player.getWorld().spawnParticle(Particle.FALLING_WATER, loc1, 40, 0.02, 0.02, 0.02, 0.0)
                player.getWorld().spawnParticle(Particle.FALLING_WATER, loc2, 40, 0.02, 0.02, 0.02, 0.0)
                player.getWorld().spawnParticle(Particle.FALLING_WATER, loc3, 40, 0.02, 0.02, 0.02, 0.0)
                player.getWorld().spawnParticle(Particle.FALLING_WATER, loc4, 40, 0.02, 0.02, 0.02, 0.0)
                loc1.add(-0.3, -0.1, 0.0)
                loc2.add(0.0, -0.1, -0.3)
                loc3.add(0.3, -0.1, 0.0)
                loc4.add(0.0, -0.1, 0.3)
                c++
            }
        }.runTaskTimer(IngeniaMC.plugin, 40L, 1L)
        object : BukkitRunnable() {
            override fun run() {
                player.setGliding(true)
                player.setGravity(false)
                player.setWalkSpeed(walkspeed)
                player.setFlySpeed(flyspeed)
            }
        }.runTaskLater(IngeniaMC.plugin, 50L)
        object : BukkitRunnable() {
            var c: Long = 0
            override fun run() {
                if (c == 100L || IngeniaPlayer(player).isInGame) {
                    cancel()
                    WandListener.gliding.remove(player)
                    if (!player.hasGravity()) player.setGravity(true)
                    if (player.isGliding()) player.setGliding(false)
                    return
                }
                player.getWorld().spawnParticle(Particle.WATER_SPLASH, player.getLocation(), 60, 0.2, 0.2, 0.2, 0.0)
                player.setVelocity(player.getEyeLocation().direction.multiply(0.75))
                c++
            }
        }.runTaskTimer(IngeniaMC.plugin, 50L, 1L)
        Cooldowns.addPlayer(player, 7500L, 8500L, 9500L, 12000L)
    }

}