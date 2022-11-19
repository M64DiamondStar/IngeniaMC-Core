package me.m64diamondstar.ingeniamccore.attractions.traincarts

import com.bergerkiller.bukkit.tc.signactions.SignAction
import me.m64diamondstar.ingeniamccore.attractions.traincarts.signs.*

object SignRegistry {
    private val signActionAttraction = SignActionAttraction()
    private val signActionGiveExp = SignActionGiveExp()
    private val signActionGiveGoldenStars = SignActionGiveGoldenStars()
    private val signActionShow = SignActionShow()
    private val signActionRidecount = SignActionRidecount()

    fun registerSigns(){
        SignAction.register(signActionAttraction)
        SignAction.register(signActionGiveExp)
        SignAction.register(signActionGiveGoldenStars)
        SignAction.register(signActionShow)
        SignAction.register(signActionRidecount)
    }

    fun unregisterSigns(){
        SignAction.unregister(signActionAttraction)
        SignAction.unregister(signActionGiveExp)
        SignAction.unregister(signActionGiveGoldenStars)
        SignAction.unregister(signActionShow)
        SignAction.unregister(signActionRidecount)
    }
}