package me.m64diamondstar.ingeniamccore.attractions.traincarts

import com.bergerkiller.bukkit.tc.signactions.SignAction
import me.m64diamondstar.ingeniamccore.attractions.traincarts.signs.SignActionAttraction

object SignRegistry {
    private val signActionAttraction = SignActionAttraction()

    fun registerSigns(){
        SignAction.register(signActionAttraction)
    }

    fun unregisterSigns(){
        SignAction.unregister(signActionAttraction)
    }
}