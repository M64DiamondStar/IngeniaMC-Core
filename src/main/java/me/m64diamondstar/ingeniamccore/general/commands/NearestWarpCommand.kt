package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import me.m64diamondstar.ingeniamccore.warps.WarpUtils
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class NearestWarpCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if(sender !is Player){
            sender.sendMessage(Messages.noPlayer())
            return false
        }

        val nearestPair = WarpUtils.getNearestPair(sender)

        (sender as Audience).sendMessage(MiniMessage.miniMessage()
            .deserialize("<${MessageType.INGENIA}>You've been teleported to ${nearestPair.first}."))

        IngeniaPlayer(sender).teleport(nearestPair.second)
        return true
    }
}