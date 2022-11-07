package me.m64diamondstar.ingeniamccore.attractions.traincarts.signs

import com.bergerkiller.bukkit.tc.events.SignActionEvent
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent
import com.bergerkiller.bukkit.tc.signactions.SignAction
import com.bergerkiller.bukkit.tc.signactions.SignActionType
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer

class SignActionGiveGoldenStars : SignAction() {

    override fun match(info: SignActionEvent): Boolean {
        return info.isType("givegs")
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

                    IngeniaPlayer(player).addBal(count)
                }
            }
        }
    }

    override fun build(event: SignChangeActionEvent): Boolean {
        return SignBuildOptions.create()
            .setName("Give Golden Stars")
            .setDescription("give Golden Stars to a player")
            .handle(event.player)
    }

}