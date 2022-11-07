package me.m64diamondstar.ingeniamccore.attractions.traincarts

import com.bergerkiller.bukkit.tc.signactions.SignAction
import me.m64diamondstar.ingeniamccore.attractions.traincarts.signs.SignActionAttraction
import me.m64diamondstar.ingeniamccore.attractions.traincarts.signs.SignActionGiveExp
import me.m64diamondstar.ingeniamccore.attractions.traincarts.signs.SignActionGiveGoldenStars

object SignRegistry {
    private val signActionAttraction = SignActionAttraction()
    private val signActionGiveExp = SignActionGiveExp()
    private val signActionGiveGoldenStars = SignActionGiveGoldenStars()

    fun registerSigns(){
        SignAction.register(signActionAttraction)
        SignAction.register(signActionGiveExp)
        SignAction.register(signActionGiveGoldenStars)
    }

    fun unregisterSigns(){
        SignAction.unregister(signActionAttraction)
        SignAction.unregister(signActionGiveExp)
        SignAction.unregister(signActionGiveGoldenStars)
    }
}