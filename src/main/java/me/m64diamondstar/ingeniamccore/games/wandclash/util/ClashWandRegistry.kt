package me.m64diamondstar.ingeniamccore.games.wandclash.util

import me.m64diamondstar.ingeniamccore.games.wandclash.clashwands.attack.Fenrir

object ClashWandRegistry {

    fun registerClashWands() {
        registerClashWand(Fenrir())
    }


    // ID, ClashWand
    private val clashWands: HashMap<String, ClashWand> = HashMap()

    fun getClashWand(clashWandID: String): ClashWand? {
        return clashWands[clashWandID]
    }

    private fun registerClashWand(clashWand: ClashWand) {
        clashWands[clashWand.getID()] = clashWand
    }

}