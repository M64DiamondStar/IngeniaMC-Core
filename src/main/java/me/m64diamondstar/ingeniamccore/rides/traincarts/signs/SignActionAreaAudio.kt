package me.m64diamondstar.ingeniamccore.rides.traincarts.signs

import com.bergerkiller.bukkit.tc.events.SignActionEvent
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent
import com.bergerkiller.bukkit.tc.signactions.SignAction
import com.bergerkiller.bukkit.tc.signactions.SignActionType
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions
import com.craftmend.openaudiomc.api.ClientApi
import com.craftmend.openaudiomc.api.MediaApi
import com.craftmend.openaudiomc.api.media.Media
import me.m64diamondstar.ingeniamccore.general.areas.Area
import me.m64diamondstar.ingeniamccore.general.areas.AreaAudioManager
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
                    if(ClientApi.getInstance().isConnected(player.uniqueId)) {
                        val client = ClientApi.getInstance().getClient(player.uniqueId)!!
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
                        MediaApi.getInstance().stopFor(client)

                        AreaUtils.getAllAreasInFormat().forEach {
                            MediaApi.getInstance().stopFor("${it}_music", client)
                        }

                        val music = currentArea?.getMusic()
                        if(music != null) {
                            val media = Media(music)
                            media.mediaId = "${ingeniaPlayer.currentAreaName}_music"
                            media.isDoPickup = true
                            media.volume = 100
                            media.fadeTime = 2
                            media.isLoop = true
                            media.keepTimeout = 3600000
                            media.startInstant = AreaAudioManager.getStartTime()
                            MediaApi.getInstance().playFor(media, client)
                        }
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