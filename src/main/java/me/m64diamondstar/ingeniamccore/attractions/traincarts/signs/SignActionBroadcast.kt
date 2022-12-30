package me.m64diamondstar.ingeniamccore.attractions.traincarts.signs

import com.bergerkiller.bukkit.tc.events.SignActionEvent
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent
import com.bergerkiller.bukkit.tc.signactions.SignAction
import com.bergerkiller.bukkit.tc.signactions.SignActionType
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions
import org.bukkit.Bukkit

class SignActionBroadcast: SignAction() {

    override fun match(info: SignActionEvent): Boolean {
        return info.isType("broadcast")
    }

    override fun execute(info: SignActionEvent) {
        if (info.isAction(SignActionType.GROUP_ENTER)) {
            Bukkit.broadcastMessage("${info.getLine(2)} ${info.getLine(3)}")
        }
    }

    override fun build(event: SignChangeActionEvent): Boolean {
        return SignBuildOptions.create()
            .setName("Broadcast")
            .setDescription("broadcast a message")
            .handle(event.player)
    }

}