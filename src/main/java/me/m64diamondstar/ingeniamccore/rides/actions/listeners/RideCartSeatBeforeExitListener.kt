package me.m64diamondstar.ingeniamccore.rides.actions.listeners

import me.m64diamondstar.ingeniamccore.rides.Ride
import me.m64diamondstar.ingeniamccore.rides.actions.RideListener
import me.m64diamondstar.ingeniamccore.rides.events.RideCartSeatBeforeExitEvent
import me.m64diamondstar.ingeniamccore.rides.expression.Expression
import org.bukkit.event.Event

class RideCartSeatBeforeExitListener(override val eventType: Class<out RideCartSeatBeforeExitEvent>) : RideListener {
    override fun getType(): String {
        return "before_seat_exit"
    }

    override fun onEventTriggered(ride: Ride, id: String, event: Event) {
        val rideEvent = event as RideCartSeatBeforeExitEvent
        val section = ride.getListenerSection(id) ?: return
        val expression = section.getString("expression")
        val executions = section.getStringList("execute")
        val channel = rideEvent.channel
        val passengerAmount = rideEvent.passengers.size

        val variables = mapOf(
            "channel" to channel,
            "passengers" to passengerAmount
        )

        val tokens = if(expression != null) Expression.tokenize(expression) else emptyList()
        val result = Expression.evaluateExpression(tokens, variables)

        if(result) executions.forEach {
            ride.execute(it)
        }

    }
}