package me.m64diamondstar.ingeniamccore.attractions.traincarts.signs

import com.bergerkiller.bukkit.tc.events.SignActionEvent
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent
import com.bergerkiller.bukkit.tc.signactions.SignAction
import com.bergerkiller.bukkit.tc.signactions.SignActionType
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.attractions.custom.Slide
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.Bukkit

/**
 * Is a check for Slide.kt and spawns a new cart when one leaves.
 */
class SignActionSlide: SignAction() {

    override fun match(info: SignActionEvent): Boolean {
        return info.isType("slide")
    }

    override fun execute(info: SignActionEvent) {

        if(!info.hasGroup()) return

        if(info.isAction(SignActionType.GROUP_ENTER)){
            info.group.properties.isSlowingDown = false
            info.group.properties.playersEnter = true
            info.group.properties.playersExit = true
        }

        if(info.isAction(SignActionType.GROUP_LEAVE)){
            val slide = Slide(info.getLine(2), info.getLine(3))
            Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, { slide.spawn() }, 100L)
        }

    }

    override fun build(event: SignChangeActionEvent): Boolean {
        event.lines.forEach {
            if(it.isEmpty()) {
                event.player.sendMessage(Colors.format(MessageType.ERROR + "Please use this format:"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "  [train]"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "   slide"))
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
            .setName("Slide Manager")
            .setDescription("tracker to activate slide carts.")
            .handle(event.player)
    }

}