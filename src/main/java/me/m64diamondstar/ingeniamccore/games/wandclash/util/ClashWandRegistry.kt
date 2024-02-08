package me.m64diamondstar.ingeniamccore.games.wandclash.util

import me.m64diamondstar.ingeniamccore.games.wandclash.clashwands.agility.BackStep
import me.m64diamondstar.ingeniamccore.games.wandclash.clashwands.agility.Launch
import me.m64diamondstar.ingeniamccore.games.wandclash.clashwands.agility.Spring
import me.m64diamondstar.ingeniamccore.games.wandclash.clashwands.attack.Fenrir

object ClashWandRegistry {

    fun registerClashWands() {
        registerClashWand(Fenrir())
        registerClashWand(BackStep())
        registerClashWand(Launch())
        registerClashWand(Spring())
    }


    // ID, ClashWand
    private val clashWands: HashMap<String, ClashWand> = HashMap()

    fun getClashWand(clashWandID: String): ClashWand? {
        return clashWands[clashWandID]
    }

    fun getClashWands(): HashMap<String, ClashWand> {
        return clashWands
    }

    private fun registerClashWand(clashWand: ClashWand) {
        clashWands[clashWand.getID()] = clashWand
    }

}