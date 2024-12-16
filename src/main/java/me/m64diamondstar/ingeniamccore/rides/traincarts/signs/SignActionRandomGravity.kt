package me.m64diamondstar.ingeniamccore.rides.traincarts.signs

import com.bergerkiller.bukkit.tc.events.SignActionEvent
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent
import com.bergerkiller.bukkit.tc.signactions.SignAction
import com.bergerkiller.bukkit.tc.signactions.SignActionType
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import kotlin.random.Random

class SignActionRandomGravity: SignAction() {

    override fun match(info: SignActionEvent): Boolean {
        return info.isType("randomgravity")
    }

    override fun execute(info: SignActionEvent) {
        if (info.isAction(SignActionType.GROUP_ENTER)) {
            if (!info.hasGroup()) return

            val times = if(Random.nextBoolean()) 1 else -1
            val distance = Random.nextDouble(0.0, info.getLine(2).toDouble()) * times
            val range = Random.nextDouble(0.0, info.getLine(3).toDouble()) * times
            info.group.properties.gravity += distance + range
        }
    }

    override fun build(event: SignChangeActionEvent): Boolean {

        event.lines.forEach {
            if(it.isEmpty()) {
                event.player.sendMessage(Colors.format(MessageType.ERROR + "Please use this format:"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "    [train]"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "  randomgravity"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "   <distance>"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "    <range>"))
                return false
            }
        }

            if(event.getLine(2).toDoubleOrNull() == null ||  event.getLine(3).toDoubleOrNull() == null){
                event.player.sendMessage(Colors.format(MessageType.ERROR + "Please use a valid numbers."))
                return false
            }

        return SignBuildOptions.create()
            .setName("Set Random Gravity")
            .setDescription("set the gravity to a random value")
            .handle(event.player)
    }

}