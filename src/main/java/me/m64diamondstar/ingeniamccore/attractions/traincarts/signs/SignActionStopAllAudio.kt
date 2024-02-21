package me.m64diamondstar.ingeniamccore.attractions.traincarts.signs

import com.bergerkiller.bukkit.tc.events.SignActionEvent
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent
import com.bergerkiller.bukkit.tc.signactions.SignAction
import com.bergerkiller.bukkit.tc.signactions.SignActionType
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.areas.AreaUtils

class SignActionStopAllAudio : SignAction() {

    override fun match(info: SignActionEvent): Boolean {
        return info.isType("stopallaudio")
    }

    override fun execute(info: SignActionEvent) {
        if (info.isAction(SignActionType.GROUP_ENTER)) {
            if (!info.hasGroup()) return
            for (member in info.group) {
                for (player in member.entity.playerPassengers) {
                    if(IngeniaMC.audioApi.isClientConnected(player.uniqueId)) {
                        val client = IngeniaMC.audioApi.getClient(player.uniqueId)!!
                        IngeniaMC.audioApi.mediaApi.stopMedia(client)
                        AreaUtils.getAllAreasInFormat().forEach {
                            IngeniaMC.audioApi.mediaApi.stopMedia(client, "${it}_music")
                        }
                    }
                }
            }
        }
    }

    override fun build(event: SignChangeActionEvent): Boolean {
        return SignBuildOptions.create()
            .setName(if (event.isCartSign) "Stop All Audio" else "Audio Stopper")
            .setDescription("stops all audio for the players inside of the cart")
            .handle(event.player)
    }

}