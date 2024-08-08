package me.m64diamondstar.ingeniamccore.games.splashbattle.listeners

import org.bukkit.Particle
import org.bukkit.entity.EntityType
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import kotlin.math.abs

class SnowballListener: Listener {

    @EventHandler
    fun onSnowballLand(event: ProjectileHitEvent){
        if(event.entityType != EntityType.SNOWBALL) return

        val waterBalloon = event.entity as Snowball

        if(waterBalloon.item.itemMeta == null) return
        if(!waterBalloon.item.itemMeta!!.hasCustomModelData()) return
        if(waterBalloon.item.itemMeta!!.customModelData != 1) return

        val direction = waterBalloon.velocity


        for(i in 0..50)
            waterBalloon.world.spawnParticle(Particle.SPLASH, waterBalloon.location, 0,
                direction.x, abs(direction.y), direction.z)
    }

}