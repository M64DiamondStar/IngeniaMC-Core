package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AudioCreditsCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if(sender !is Player){
            sender.sendMessage(Messages.noPlayer())
            return false
        }

        (sender as Audience).sendMessage(
            MiniMessage.miniMessage().deserialize(
                "<${MessageType.INGENIA}>Check out our <#ffffff>" +
                        "<click:open_url:https://ingeniamc.net/music-usage>website</click>" +
                        "<${MessageType.INGENIA}> for all the credits.")
        )

        return false
    }
}