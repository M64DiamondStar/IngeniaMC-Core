package me.m64diamondstar.ingeniamccore.rides.traincarts.signs

import com.bergerkiller.bukkit.tc.events.SignActionEvent
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent
import com.bergerkiller.bukkit.tc.signactions.SignAction
import com.bergerkiller.bukkit.tc.signactions.SignActionType
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions
import me.m64diamondstar.ingeniamccore.rides.Ride
import me.m64diamondstar.ingeniamccore.rides.events.RideCartEnterEvent
import me.m64diamondstar.ingeniamccore.rides.events.RideCartLeaveEvent
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.sendMini
import org.bukkit.Bukkit

class SignActionRide: SignAction() {
    override fun match(info: SignActionEvent): Boolean {
        return info.isType("ride")
    }

    override fun execute(info: SignActionEvent) {
        if(!info.isPowered() || !info.hasGroup()) return

        // Create ride, returns if it doesn't exist
        val ride = Ride.getRide(info.getLine(2)) ?: return

        // Check which task it should execute
        when(info.getLine(1).lowercase()){
            "ride detector" -> {
                when(info.action){
                    SignActionType.GROUP_ENTER -> {
                        val rideCartEnterEvent = RideCartEnterEvent(ride, info.getLine(3), info.group.map { member -> member.entity.playerPassengers }.flatten())
                        Bukkit.getPluginManager().callEvent(rideCartEnterEvent) // Send a cart enter event,
                        // this event covers carts entering a certain detector sign with a channel
                    }

                    SignActionType.GROUP_LEAVE -> {
                        val rideCartLeaveEvent = RideCartLeaveEvent(ride, info.getLine(3), info.group.map { member -> member.entity.playerPassengers }.flatten())
                        Bukkit.getPluginManager().callEvent(rideCartLeaveEvent) // Send a cart leave event,
                        // this event covers carts leaving a certain detector sign with a channel
                    }

                    else -> {}
                }
            }
        }
    }

    override fun build(event: SignChangeActionEvent): Boolean {
        when(event.getLine(1).lowercase()){

            /*
                [train]
             ride detector
                <ride>
               <channel>
             */
            "ride detector" -> {
                if(Ride.getRide(event.getLine(2)) == null){
                    event.player.sendMini(MessageType.ERROR + "Please enter a valid ride id on the third line.")
                    return false
                }
                if(event.getLine(3).isNullOrEmpty()){
                    event.player.sendMini(MessageType.ERROR + "Please enter a channel name on the fourth line.")
                    return false
                }
                return SignBuildOptions.create()
                    .setDescription("place a detector which will send signals to ride listeners. " +
                            "This detector will send signals through channel '${event.getLine(3)}' for ride '${event.getLine(2)}'")
                    .setName("Ride Detector")
                    .handle(event)
            }
        }
        return false
    }
}