package me.m64diamondstar.ingeniamccore.attractions.traincarts.signs

import com.bergerkiller.bukkit.tc.events.SignActionEvent
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent
import com.bergerkiller.bukkit.tc.signactions.SignAction
import com.bergerkiller.bukkit.tc.signactions.SignActionType
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions
import me.m64diamondstar.ingeniamccore.shows.utils.Show
import me.m64diamondstar.ingeniamccore.shows.utils.ShowUtils
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType

class SignActionShow: SignAction() {

    override fun match(info: SignActionEvent): Boolean {
        return info.isType("show")
    }

    override fun execute(info: SignActionEvent) {
        if (info.isAction(SignActionType.GROUP_ENTER)) {
            val show = Show(info.getLine(2), info.getLine(3), null)
            show.play()
        }
    }

    override fun build(event: SignChangeActionEvent): Boolean {

        event.lines.forEach {
            if(it.isEmpty()) {
                event.player.sendMessage(Colors.format(MessageType.ERROR + "Please use this format:"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "  [train]"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "    show"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "<category>"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "   <name>"))
                return false
            }
        }

        if(!ShowUtils.existsCategory(event.getLine(2)) || !ShowUtils.existsShow(event.getLine(2), event.getLine(3))){
            event.player.sendMessage(Colors.format(MessageType.ERROR + "This show or category does not exist."))
            return false
        }

        return SignBuildOptions.create()
            .setName("Play Show")
            .setDescription("play a show when this gets activated by a train")
            .handle(event.player)
    }
}