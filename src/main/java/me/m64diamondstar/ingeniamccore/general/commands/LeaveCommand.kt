package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.games.PhysicalGameType
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class LeaveCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) {
            sender.sendMessage(Messages.noPlayer())
            return false
        }

        val ingeniaPlayer = IngeniaPlayer(player = sender)

        if(!ingeniaPlayer.isInGame){
            ingeniaPlayer.sendMessage(MessageType.ERROR + "You are not in a game.")
            return false
        }

        else{
            when (ingeniaPlayer.game){

                PhysicalGameType.PARKOUR -> {
                    ingeniaPlayer.isInGameLeavingState = true
                }

                else -> {
                    ingeniaPlayer.sendMessage(MessageType.ERROR + "You are not in a game.")
                }
            }
        }

        return false
    }
}