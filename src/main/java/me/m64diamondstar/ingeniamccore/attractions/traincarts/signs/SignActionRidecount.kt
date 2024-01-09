package me.m64diamondstar.ingeniamccore.attractions.traincarts.signs

import com.bergerkiller.bukkit.tc.events.SignActionEvent
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent
import com.bergerkiller.bukkit.tc.signactions.SignAction
import com.bergerkiller.bukkit.tc.signactions.SignActionType
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions
import me.m64diamondstar.ingeniamccore.attractions.Attraction
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages

class SignActionRidecount : SignAction() {

    override fun match(info: SignActionEvent): Boolean {
        return info.isType("ridecount")
    }

    override fun execute(info: SignActionEvent) {
        if (info.isAction(SignActionType.GROUP_ENTER)) {
            if (!info.hasGroup()) return

            val attraction = Attraction(info.getLine(2), info.getLine(3))

            for (member in info.group) {
                for (player in member.entity.playerPassengers) {
                    attraction.addRidecount(player, 1)
                    player.sendMessage(Messages.currentRidecount(attraction.getRidecount(player)))
                }
            }

            for (member in info.group) {
                for (player in member.entity.playerPassengers) {
                    attraction.spawnRidecountSign(player)
                }
            }
        }
    }

    override fun build(event: SignChangeActionEvent): Boolean {
        event.lines.forEach {
            if(it.isEmpty()) {
                event.player.sendMessage(Colors.format(MessageType.ERROR + "Please use this format:"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "  [train]"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "ridecount"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "<category>"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "   <name>"))
                return false
            }
        }

        if(!AttractionUtils.existsCategory(event.getLine(2)) || !AttractionUtils.existsAttraction(event.getLine(2), event.getLine(3))){
            event.player.sendMessage(Colors.format(MessageType.ERROR + "This attraction or category does not exist."))
            return false
        }

        return SignBuildOptions.create()
            .setName("Ridecount Adder")
            .setDescription("add ridecount to passengers")
            .handle(event.player)
    }

}