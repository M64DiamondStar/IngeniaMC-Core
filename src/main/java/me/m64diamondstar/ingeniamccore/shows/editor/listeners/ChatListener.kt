package me.m64diamondstar.ingeniamccore.shows.editor.listeners

import me.m64diamondstar.ingeniamccore.shows.editor.effect.EditEffectGui
import me.m64diamondstar.ingeniamccore.shows.editor.utils.EditingPlayers
import me.m64diamondstar.ingeniamccore.shows.utils.ParameterType
import me.m64diamondstar.ingeniamccore.shows.EffectShow
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatListener: Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onChat(event: AsyncPlayerChatEvent){
        val player = event.player

        if(!EditingPlayers.contains(player)) return

        event.isCancelled = true

        val showCategory = EditingPlayers.get(player)!!.first.getCategory()
        val showName = EditingPlayers.get(player)!!.first.getName()
        val effectShow = EffectShow(showCategory, showName, null)

        val id = EditingPlayers.get(player)!!.second
        val parameter = EditingPlayers.get(player)!!.third

        if(event.message.equals("cancel", ignoreCase = true)){
            player.sendMessage(Colors.format(MessageType.SUCCESS + "Cancelled edit."))
            val editEffectGui = EditEffectGui(player, id, effectShow)
            editEffectGui.open()
            EditingPlayers.remove(player)
        }else{

            val value = event.message

            if(ParameterType.valueOf(parameter.uppercase()).getFormat().isPossible(value)){
                effectShow.getEffect(id)!!.getSection().set(parameter, ParameterType.valueOf(parameter.uppercase()).getFormat().convertToFormat(value))
                effectShow.reloadConfig()

                player.sendMessage(Colors.format(MessageType.SUCCESS + "Edited parameter."))
                val editEffectGui = EditEffectGui(player, id, effectShow)
                editEffectGui.open()
                EditingPlayers.remove(player)
            }else{
                player.sendMessage(Colors.format(MessageType.ERROR + "The value entered is not possible."))
                player.sendMessage(Colors.format(MessageType.ERROR + "You need to enter a(n) $parameter, please read " +
                        "the info above."))
            }
        }

    }

}