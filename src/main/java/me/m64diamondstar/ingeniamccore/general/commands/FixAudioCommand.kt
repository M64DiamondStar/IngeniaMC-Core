package me.m64diamondstar.ingeniamccore.general.commands

import com.craftmend.openaudiomc.generic.media.objects.MediaOptions
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.areas.Area
import me.m64diamondstar.ingeniamccore.general.areas.AreaUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FixAudioCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if(sender !is Player){
            sender.sendMessage(Messages.noPlayer())
            return false
        }

        if(!IngeniaMC.audioApi.isClientConnected(sender.uniqueId)){
            sender.sendMessage(Colors.format(MessageType.ERROR + "You are not connected to the Audio Server. Please use /audio."))
            return false
        }

        val client = IngeniaMC.audioApi.getClient(sender.uniqueId)!!
        val ingeniaPlayer = IngeniaPlayer(sender)
        var currentArea: Area? = null

        for(area in AreaUtils.getAllAreas()){
            if(area.area != null) {
                if (area.area!!.contains(sender.location.blockX, sender.location.blockZ) &&
                    sender.location.blockY in area.minY..area.maxY
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

        return false
    }

}