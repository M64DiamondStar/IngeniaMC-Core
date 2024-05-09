package me.m64diamondstar.ingeniamccore.general.commands

import com.craftmend.openaudiomc.api.ClientApi
import com.craftmend.openaudiomc.api.MediaApi
import com.craftmend.openaudiomc.api.media.Media
import me.m64diamondstar.ingeniamccore.general.areas.Area
import me.m64diamondstar.ingeniamccore.general.areas.AreaAudioManager
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

        if(!ClientApi.getInstance().isConnected(sender.uniqueId)){
            sender.sendMessage(Colors.format(MessageType.ERROR + "You are not connected to the Audio Server. Please use /audio."))
            return false
        }

        val client = ClientApi.getInstance().getClient(sender.uniqueId)!!
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
            // media.startAtMillis = AreaAudioManager.getStartMillis(area = currentArea!!).toInt()
            media.startInstant = AreaAudioManager.getStartTime()
            MediaApi.getInstance().playFor(media, client)
        }

        return false
    }

}