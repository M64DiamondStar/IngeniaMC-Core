package me.m64diamondstar.ingeniamccore.wands.wands

interface Wand {

    fun getDisplayName(): String
    fun getCustomModelData(): Int
    fun hasPermission(): Boolean
    fun run()

}