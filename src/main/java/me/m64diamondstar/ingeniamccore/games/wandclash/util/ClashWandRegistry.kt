package me.m64diamondstar.ingeniamccore.games.wandclash.util

import me.m64diamondstar.ingeniamccore.games.wandclash.spells.agility.BackStep
import me.m64diamondstar.ingeniamccore.games.wandclash.spells.agility.Launch
import me.m64diamondstar.ingeniamccore.games.wandclash.spells.agility.Spring
import me.m64diamondstar.ingeniamccore.games.wandclash.spells.attack.Fenrir

object ClashWandRegistry {

    fun registerClashWands() {
        registerClashWand(Fenrir())
        registerClashWand(BackStep())
        registerClashWand(Launch())
        registerClashWand(Spring())
    }


    // ID, ClashWand
    private val wandsClashSpell: HashMap<String, WandClashSpell> = HashMap()

    fun getClashWand(clashWandID: String): WandClashSpell? {
        return wandsClashSpell[clashWandID]
    }

    fun getClashWands(): HashMap<String, WandClashSpell> {
        return wandsClashSpell
    }

    private fun registerClashWand(wandClashSpell: WandClashSpell) {
        wandsClashSpell[wandClashSpell.getID()] = wandClashSpell
    }

}