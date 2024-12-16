package me.m64diamondstar.ingeniamccore.rides

import com.bergerkiller.bukkit.tc.signactions.SignAction
import me.m64diamondstar.ingeniamccore.rides.actions.RideExecution
import me.m64diamondstar.ingeniamccore.rides.actions.executions.EffectMasterExecution
import me.m64diamondstar.ingeniamccore.rides.actions.executions.GatesExecution
import me.m64diamondstar.ingeniamccore.rides.actions.executions.PowerChannelPulseExecution
import me.m64diamondstar.ingeniamccore.rides.actions.executions.PowerChannelToggleExecution
import me.m64diamondstar.ingeniamccore.rides.traincarts.signs.SignActionAreaAudio
import me.m64diamondstar.ingeniamccore.rides.traincarts.signs.SignActionGiveExp
import me.m64diamondstar.ingeniamccore.rides.traincarts.signs.SignActionGiveGoldenStars
import me.m64diamondstar.ingeniamccore.rides.traincarts.signs.SignActionRandomGravity
import me.m64diamondstar.ingeniamccore.rides.traincarts.signs.SignActionRide
import me.m64diamondstar.ingeniamccore.rides.traincarts.signs.SignActionStopAllAudio

object RideInitialization {

    private val signActionAreaAudio = SignActionAreaAudio()
    private val signActionGiveExp = SignActionGiveExp()
    private val signActionGiveGoldenStars = SignActionGiveGoldenStars()
    private val signActionRandomGravity = SignActionRandomGravity()
    private val signActionRide = SignActionRide()
    private val signActionStopAllAudio = SignActionStopAllAudio()

    /**
     * Initialize all rides, leaderboards, conditions, executives, ...
     */
    fun initialize(){
        Ride.loadFromFile()
        initializeExecutives()
    }

    /**
     * Initialize all executives used to execute certain actions for rides
     */
    private fun initializeExecutives(){
        RideExecution.register(EffectMasterExecution())
        RideExecution.register(GatesExecution())
        RideExecution.register(PowerChannelPulseExecution())
        RideExecution.register(PowerChannelToggleExecution())
    }

    /**
     * Initialize all the SignAction classes for traincarts
     */
    private fun initializeSignAction(){
        SignAction.register(signActionAreaAudio)
        SignAction.register(signActionGiveExp)
        SignAction.register(signActionGiveGoldenStars)
        SignAction.register(signActionRandomGravity)
        SignAction.register(signActionRide)
        SignAction.register(signActionStopAllAudio)
    }

    /**
     * Disable and save all rides, sign actions, ...
     */
    fun disable(){
        disableSignAction()
    }

    /**
     * Disable all the SignAction classes for traincarts
     */
    private fun disableSignAction(){
        SignAction.unregister(signActionAreaAudio)
        SignAction.unregister(signActionGiveExp)
        SignAction.unregister(signActionGiveGoldenStars)
        SignAction.unregister(signActionRandomGravity)
        SignAction.unregister(signActionRide)
        SignAction.unregister(signActionStopAllAudio)
    }

}