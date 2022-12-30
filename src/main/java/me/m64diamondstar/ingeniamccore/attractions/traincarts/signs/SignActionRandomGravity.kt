package me.m64diamondstar.ingeniamccore.attractions.traincarts.signs

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

            val random = Random.nextDouble(info.getLine(2).toDouble(), info.getLine(3).toDouble())
            info.group.properties.gravity += random
        }
    }

    override fun build(event: SignChangeActionEvent): Boolean {

        event.lines.forEach {
            if(it.isEmpty()) {
                event.player.sendMessage(Colors.format(MessageType.ERROR + "Please use this format:"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "    [train]"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "  randomgravity"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + " <lowest random>"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "<highest random>"))
                return false
            }
        }

        try{
            if(event.getLine(2).toDouble() >= event.getLine(3).toDouble()){
                event.player.sendMessage(Colors.format(MessageType.ERROR +
                        "The second number has to be bigger than the first one."))
                return false
            }
        }catch (e: NumberFormatException){
            event.player.sendMessage(Colors.format(MessageType.ERROR + "Please use a valid numbers."))
            return false
        }

        return SignBuildOptions.create()
            .setName("Set Random Gravity")
            .setDescription("set the gravity to a random value between 2 numbers")
            .handle(event.player)
    }

}