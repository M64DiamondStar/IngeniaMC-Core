package me.m64diamondstar.ingeniamccore.attractions.traincarts

import com.bergerkiller.bukkit.tc.signactions.SignAction
import me.m64diamondstar.ingeniamccore.attractions.traincarts.signs.*

object SignRegistry {
    private val signActionAttraction = SignActionAttraction()
    private val signActionGiveExp = SignActionGiveExp()
    private val signActionGiveGoldenStars = SignActionGiveGoldenStars()
    private val signActionRidecount = SignActionRidecount()
    private val signActionSlide = SignActionSlide()
    private val signActionRandomGravity = SignActionRandomGravity()
    private val signActionBroadcast = SignActionBroadcast()
    private val signActionParktrain = SignActionParktrain()
    private val signActionStopAllAudio = SignActionStopAllAudio()
    private val signActionAreaAudio = SignActionAreaAudio()

    fun registerSigns(){
        SignAction.register(signActionAttraction)
        SignAction.register(signActionGiveExp)
        SignAction.register(signActionGiveGoldenStars)
        SignAction.register(signActionRidecount)
        SignAction.register(signActionSlide)
        SignAction.register(signActionRandomGravity)
        SignAction.register(signActionBroadcast)
        SignAction.register(signActionParktrain)
        SignAction.register(signActionStopAllAudio)
        SignAction.register(signActionAreaAudio)
    }

    fun unregisterSigns(){
        SignAction.unregister(signActionAttraction)
        SignAction.unregister(signActionGiveExp)
        SignAction.unregister(signActionGiveGoldenStars)
        SignAction.unregister(signActionRidecount)
        SignAction.unregister(signActionSlide)
        SignAction.unregister(signActionRandomGravity)
        SignAction.unregister(signActionBroadcast)
        SignAction.unregister(signActionParktrain)
        SignAction.unregister(signActionStopAllAudio)
        SignAction.unregister(signActionAreaAudio)
    }
}