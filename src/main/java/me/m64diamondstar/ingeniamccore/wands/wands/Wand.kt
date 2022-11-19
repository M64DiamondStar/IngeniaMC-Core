package me.m64diamondstar.ingeniamccore.wands.wands

import org.bukkit.entity.Player

interface Wand {

    fun getDisplayName(): String
    fun getCustomModelData(): Int
    fun hasPermission(player: Player): Boolean
    fun run(player: Player)

}