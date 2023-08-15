package me.m64diamondstar.ingeniamccore.games.splashbattle.listeners

import me.m64diamondstar.ingeniamccore.games.splashbattle.SplashAbilities
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CrossbowMeta

class GunListener: Listener {

    @EventHandler
    fun onBowShootEvent(event: EntityShootBowEvent) {

        // Checks to see if (cross)bow is a water gun
        if (event.bow == null) return
        if(event.bow!!.type != Material.CROSSBOW) return
        if(event.bow!!.itemMeta == null) return
        if(!event.bow!!.itemMeta!!.hasCustomModelData()) return
        if(!(event.bow!!.itemMeta!! as CrossbowMeta).hasChargedProjectiles()) return

        // Cancel the event (this only prevents the arrow from being shot, the bow still un-charges)
        event.isCancelled = true

        // Create a new loaded bow item
        val bow = event.bow!!
        val meta = bow.itemMeta as CrossbowMeta

        meta.setChargedProjectiles(listOf(ItemStack(Material.ARROW)))
        bow.itemMeta = meta

        // Put the bow in the hand of the player.
        (event.entity as Player).inventory.setItem(event.hand, bow)


        if(bow.itemMeta!!.customModelData == 1)
            SplashAbilities.shootPistol(player = event.entity as Player)

        if(bow.itemMeta!!.customModelData == 2)
            SplashAbilities.shootGun(player = event.entity as Player)
    }

}