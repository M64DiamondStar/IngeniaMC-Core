package me.m64diamondstar.ingeniamccore.attractions.traincarts.signs

import com.bergerkiller.bukkit.tc.events.SignActionEvent
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent
import com.bergerkiller.bukkit.tc.signactions.SignAction
import com.bergerkiller.bukkit.tc.signactions.SignActionType
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions
import com.craftmend.openaudiomc.generic.media.objects.MediaOptions
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.areas.Area
import me.m64diamondstar.ingeniamccore.general.areas.AreaUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer

class SignActionAreaAudio : SignAction() {

    override fun match(info: SignActionEvent): Boolean {
        return info.isType("areaaudio")
    }

    override fun execute(info: SignActionEvent) {
        if (info.isAction(SignActionType.GROUP_ENTER)) {
            if (!info.hasGroup()) return
            for (member in info.group) {
                for (player in member.entity.playerPassengers) {
                    if(IngeniaMC.audioApi.isClientConnected(player.uniqueId)) {
                        val client = IngeniaMC.audioApi.getClient(player.uniqueId)!!
                        val ingeniaPlayer = IngeniaPlayer(player)
                        var currentArea: Area? = null

                        for(area in AreaUtils.getAllAreas()){
                            if(area.area != null) {
                                if (area.area!!.contains(player.location.blockX, player.location.blockZ) &&
                                    player.location.blockY in area.minY..area.maxY
                                ) {
                                    if (currentArea == null)
                                        currentArea = area
                                    else {
                                        if (area.weight > currentArea.weight)
                                            currentArea = area
                                    }
                                }
                            }
                        }
                        IngeniaMC.audioApi.mediaApi.stopMedia(client)

                        AreaUtils.getAllAreasInFormat().forEach {
                            IngeniaMC.audioApi.mediaApi.stopMedia(client, "${it}_music")
                        }

                        val options = MediaOptions()
                        options.id = "${ingeniaPlayer.currentAreaName}_music"
                        options.isPickUp = true
                        options.volume = 100
                        options.fadeTime = 2
                        options.isLoop = true
                        options.expirationTimeout = 3600000
                        val music = currentArea?.getMusic()
                        if(music != null)
                            IngeniaMC.audioApi.mediaApi.playMedia(client, music, options)
                    }
                }
            }
        }
    }

    override fun build(event: SignChangeActionEvent): Boolean {
        return SignBuildOptions.create()
            .setName(if (event.isCartSign) "Stop All Audio" else "Audio Area Player")
            .setDescription("start playing the area music after a ride")
            .handle(event.player)
    }

}