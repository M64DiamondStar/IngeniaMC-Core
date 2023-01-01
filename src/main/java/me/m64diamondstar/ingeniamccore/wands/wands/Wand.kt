package me.m64diamondstar.ingeniamccore.wands.wands

import org.bukkit.entity.Player

interface Wand {

    fun getDisplayName(): String
    fun getCustomModelData(): Int
    fun hasPermission(player: Player): Boolean
    fun run(player: Player)

    /* Clean runnable

    object : BukkitRunnable() {

        override fun run() {
            if(IngeniaPlayer(player).isInGame){
                this.cancel()
                return
            }


        }
    }.runTaskTimer(IngeniaMC.plugin, 0L, 1L)


    // Clean runnable with counter
    object : BukkitRunnable() {

        var c = 0

        override fun run() {
            if(IngeniaPlayer(player).isInGame){
                this.cancel()
                return
            }



            c++
        }
    }.runTaskTimer(IngeniaMC.plugin, 0L, 1L)
    */

}