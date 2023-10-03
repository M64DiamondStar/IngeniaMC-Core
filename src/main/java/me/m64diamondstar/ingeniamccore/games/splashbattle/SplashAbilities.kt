package me.m64diamondstar.ingeniamccore.games.splashbattle

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import org.bukkit.Particle
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.scheduler.BukkitRunnable

object SplashAbilities {

    private val pistolMap: HashMap<Player, Long> = HashMap()
    private val gunMap: HashMap<Player, Long> = HashMap()
    private val outOfAmmo: HashMap<Player, Long> = HashMap()

    fun shootPistol(player: Player) {
        // Check if buckets are empty
        if(SplashBattleUtils.getAmmo(player) <= 0){
            if(!outOfAmmo.containsKey(player) || (outOfAmmo.containsKey(player) && System.currentTimeMillis() - outOfAmmo[player]!! > 2000)) {
                player.sendMessage(Colors.format("#3671baYou're out of ammo! Sneak while looking at water to refill."))
                outOfAmmo[player] = System.currentTimeMillis()
            }
            return
        }

        // Check for cooldown
        if(pistolMap.containsKey(player)){
            if(System.currentTimeMillis() - pistolMap[player]!! < 400)
                return
        }
        pistolMap[player] = System.currentTimeMillis()

        // Remove one ammo
        SplashBattleUtils.removeAmmo(player, 1)

        // Create the bullet
        val bullet = player.world.spawnEntity(player.eyeLocation.add(player.eyeLocation.direction), EntityType.SNOWBALL) as Snowball
        bullet.item = SplashBattleItems.getInvisibleBalloon()
        bullet.shooter = player

        bullet.velocity = player.eyeLocation.direction.multiply(1.5)

        object: BukkitRunnable() {
            override fun run() {
                if(!bullet.isDead)
                    bullet.world.spawnParticle(Particle.WATER_SPLASH, bullet.location, 30, 0.0, 0.0, 0.0, 0.0, null, false)
                else
                    this.cancel()
            }
        }.runTaskTimer(IngeniaMC.plugin, 0L, 0L)
    }

    fun shootGun(player: Player) {
        // Check if buckets are empty
        if(SplashBattleUtils.getAmmo(player) <= 0){
            if(!outOfAmmo.containsKey(player) || (outOfAmmo.containsKey(player) && System.currentTimeMillis() - outOfAmmo[player]!! > 2000)) {
                player.sendMessage(Colors.format("#3671baYou're out of ammo! Sneak while looking at water to refill."))
                outOfAmmo[player] = System.currentTimeMillis()
            }
            return
        }

        // Check if buckets are full enough to use ability
        if(SplashBattleUtils.getAmmo(player) < 2){
            if(!outOfAmmo.containsKey(player) || (outOfAmmo.containsKey(player) && System.currentTimeMillis() - outOfAmmo[player]!! > 2000)) {
                player.sendMessage(Colors.format("#3671baNot enough water in your buckets! Refill your ammo for a"))
                player.sendMessage(Colors.format("#3671babit by looking at water and holding sneak."))
                outOfAmmo[player] = System.currentTimeMillis()
            }
            return
        }

        // Check if player is on cooldown
        if(gunMap.containsKey(player)){
            if(System.currentTimeMillis() - gunMap[player]!! < 100)
                return
        }
        gunMap[player] = System.currentTimeMillis()

        // Remove one ammo
        SplashBattleUtils.removeAmmo(player, 2)

        // Create the bullet
        val bullet = player.world.spawnEntity(player.eyeLocation.add(player.eyeLocation.direction), EntityType.SNOWBALL) as Snowball
        bullet.item = SplashBattleItems.getInvisibleBalloon()
        bullet.shooter = player

        bullet.velocity = player.eyeLocation.direction.multiply(2.5)

        object: BukkitRunnable() {
            override fun run() {
                if(!bullet.isDead)
                    bullet.world.spawnParticle(Particle.WATER_SPLASH, bullet.location, 30, 0.0, 0.0, 0.0, 0.0, null, false)
                else
                    this.cancel()
            }
        }.runTaskTimer(IngeniaMC.plugin, 0L, 0L)
    }

}