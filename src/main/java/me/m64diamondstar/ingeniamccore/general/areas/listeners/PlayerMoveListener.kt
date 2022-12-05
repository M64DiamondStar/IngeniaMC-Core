package me.m64diamondstar.ingeniamccore.general.areas.listeners

import com.craftmend.openaudiomc.generic.media.objects.MediaOptions
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.areas.Area
import me.m64diamondstar.ingeniamccore.general.areas.AreaUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class PlayerMoveListener: Listener {

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent){

        if(event.from.blockX == event.to?.blockX && event.from.blockY == event.to?.blockY && event.from.blockZ == event.to?.blockZ )
            return

        val player = event.player
        val ingeniaPlayer = IngeniaPlayer(player)

        var currentArea: Area? = null

        for(category in AreaUtils.getCategories()){
            for(areaName in AreaUtils.getAreas()[category.name]?.keys!!){
                val area = Area(category.name, areaName)

                if (AreaUtils.getAreas()[category.name]?.get(areaName)!!.contains(player.location.blockX, player.location.blockZ) &&
                    player.location.blockY in area.minY..area.maxY){
                    if(currentArea == null)
                        currentArea = area
                    else{
                        if(area.weight > currentArea.weight)
                            currentArea = area
                    }
                }
            }
        }

        val client = IngeniaMC.audioApi.getClient(player.uniqueId)

        if(currentArea == null){
            ingeniaPlayer.currentAreaName = null
            IngeniaMC.audioApi.mediaApi.stopMedia(client)
            return
        }

        //Check if player's saved area is null
        if(ingeniaPlayer.currentAreaName != null)
        //If current player area and move area are the same, code has to stop
            if(ingeniaPlayer.currentAreaName == "${currentArea.category}/${currentArea.name}")
                return

        IngeniaMC.audioApi.mediaApi.stopMedia(client)

        val textComponent = TextComponent(currentArea.displayName)
        textComponent.color = ChatColor.of(MessageType.PLAYER_UPDATE)

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent)
        ingeniaPlayer.currentAreaName = "${currentArea.category}/${currentArea.name}"

        val options = MediaOptions()
        options.volume = 100
        options.fadeTime = 2
        options.isLoop = true
        val music = currentArea.getMusic()
        if(music != null)
            IngeniaMC.audioApi.mediaApi.playMedia(client, music, options)

    }


}