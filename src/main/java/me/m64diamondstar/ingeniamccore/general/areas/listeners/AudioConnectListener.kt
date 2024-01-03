package me.m64diamondstar.ingeniamccore.general.areas.listeners

import com.craftmend.openaudiomc.api.impl.event.events.ClientConnectEvent
import com.craftmend.openaudiomc.generic.media.objects.MediaOptions
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.areas.AreaUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import org.bukkit.Bukkit

object AudioConnectListener {

    fun startListeners(){
        IngeniaMC.audioApi.eventDriver
            .on(ClientConnectEvent::class.java)
            .setHandler {

                IngeniaMC.audioApi.mediaApi.stopMedia(it.client)

                val player = Bukkit.getPlayer(it.client.owner.uniqueId) ?: return@setHandler
                val ingeniaPlayer = IngeniaPlayer(player)
                val area = AreaUtils.getAreaFromName(ingeniaPlayer.currentAreaName) ?: return@setHandler

                val options = MediaOptions()
                options.id = "${ingeniaPlayer.currentAreaName}_music"
                options.isPickUp = true
                options.volume = 100
                options.fadeTime = 2
                options.isLoop = true
                options.expirationTimeout = 3600000
                val music = area.getMusic()
                if(music != null)
                    IngeniaMC.audioApi.mediaApi.playMedia(it.client, music, options)

            }
    }

}