package me.m64diamondstar.ingeniamccore.attractions.traincarts

import com.bergerkiller.bukkit.tc.signactions.SignAction
import me.m64diamondstar.ingeniamccore.attractions.traincarts.signs.SignActionAttraction
import me.m64diamondstar.ingeniamccore.attractions.traincarts.signs.SignActionGiveExp
import me.m64diamondstar.ingeniamccore.attractions.traincarts.signs.SignActionGiveGoldenStars
import me.m64diamondstar.ingeniamccore.attractions.traincarts.signs.SignActionShow

object SignRegistry {
    private val signActionAttraction = SignActionAttraction()
    private val signActionGiveExp = SignActionGiveExp()
    private val signActionGiveGoldenStars = SignActionGiveGoldenStars()
    private val signActionShow = SignActionShow()

    fun registerSigns(){
        SignAction.register(signActionAttraction)
        SignAction.register(signActionGiveExp)
        SignAction.register(signActionGiveGoldenStars)
        SignAction.register(signActionShow)
    }

    fun unregisterSigns(){
        SignAction.unregister(signActionAttraction)
        SignAction.unregister(signActionGiveExp)
        SignAction.unregister(signActionGiveGoldenStars)
        SignAction.unregister(signActionShow)
    }
}