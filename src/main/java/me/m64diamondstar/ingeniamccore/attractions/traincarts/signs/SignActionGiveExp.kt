package me.m64diamondstar.ingeniamccore.attractions.traincarts.signs

import com.bergerkiller.bukkit.tc.events.SignActionEvent
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent
import com.bergerkiller.bukkit.tc.signactions.SignAction
import com.bergerkiller.bukkit.tc.signactions.SignActionType
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer

class SignActionGiveExp : SignAction() {

    override fun match(info: SignActionEvent): Boolean {
        return info.isType("giveexp")
    }

    override fun execute(info: SignActionEvent) {
        if (info.isAction(SignActionType.GROUP_ENTER)) {
            if (!info.hasGroup()) return
            for (member in info.group) {
                for (player in member.entity.playerPassengers) {
                    val count: Long = try {
                        info.getLine(2).toLong()
                    } catch (e: NumberFormatException) {
                        1
                    }

                    IngeniaPlayer(player).addExp(count)
                }
            }
        }
    }

    override fun build(event: SignChangeActionEvent): Boolean {
        return SignBuildOptions.create()
            .setName(if (event.isCartSign) "Give Exp" else "Exp Giver")
            .setDescription("Gives exp to the player in a train when it goes over this sign!")
            .handle(event.player)
    }

}