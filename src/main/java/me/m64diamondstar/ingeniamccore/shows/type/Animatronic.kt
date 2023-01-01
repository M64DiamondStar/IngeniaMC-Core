package me.m64diamondstar.ingeniamccore.shows.type

import me.m64diamondstar.ingeniamccore.shows.utils.EffectType
import me.m64diamondstar.ingeniamccore.shows.utils.Show
import me.thundertnt33.animatronics.api.Animatronic

class Animatronic(show: Show, id: Int): EffectType(show, id) {

    override fun execute() {
        val name = getSection().getString("Name")!!
        val animatronic = Animatronic(name)
        animatronic.start()
    }

    override fun getType(): Types{
        return Types.ANIMATRONIC
    }

    override fun isSync(): Boolean {
        return true
    }
}