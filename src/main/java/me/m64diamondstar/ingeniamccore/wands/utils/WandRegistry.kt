package me.m64diamondstar.ingeniamccore.wands.utils

import me.m64diamondstar.ingeniamccore.wands.wands.*

object WandRegistry {

    private val wands: MutableMap<String, Wand> = HashMap()

    private fun registerWand(id: String, wand: Wand){
        wands[id] = wand
    }

    fun getWand(id: String): Wand? {
        return wands[id]
    }

    fun getWands(): MutableMap<String, Wand> {
        return wands
    }

    fun registerWands(){
        registerWand("air", Air())
        registerWand("antigravity", AntiGravity())
        registerWand("blocklauncher", BlockLauncher())
        registerWand("bouncer", Bouncer())
        registerWand("bush", Bush())
        registerWand("cloak", Cloak())
        registerWand("earth", Earth())
        registerWand("fire", Fire())
        registerWand("fly", Fly())
        registerWand("grapple", Grapple())
        registerWand("happiness", Happiness())
        registerWand("holytomato", HolyTomato())
        registerWand("launch", Launch())
        registerWand("music", Music())
        registerWand("sled", Sled())
        registerWand("snowcannon", SnowCannon())
        registerWand("snowexplosion", SnowExplosion())
        registerWand("speed", Speed())
        registerWand("tnt", TNT())
        registerWand("water", Water())
    }

}