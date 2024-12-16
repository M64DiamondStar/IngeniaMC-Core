package me.m64diamondstar.ingeniamccore.rides.actions.listeners

import me.m64diamondstar.ingeniamccore.rides.Ride
import me.m64diamondstar.ingeniamccore.rides.actions.RideListener
import me.m64diamondstar.ingeniamccore.rides.events.RideCartEnterEvent
import me.m64diamondstar.ingeniamccore.rides.expression.Expression
import org.bukkit.event.Event

class RideCartEnterListener(override val eventType: Class<out RideCartEnterEvent>) : RideListener {
    override fun getType(): String {
        return "cart_enter"
    }

    override fun onEventTriggered(ride: Ride, id: String, event: Event) {
        val rideEvent = event as RideCartEnterEvent
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