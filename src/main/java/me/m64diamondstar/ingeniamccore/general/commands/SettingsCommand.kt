package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.general.player.settings.SettingsInventory
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SettingsCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player){
            sender.sendMessage(Messages.noPlayer())
            return false
        }

        val settingsInventory = SettingsInventory(IngeniaPlayer(sender))
        settingsInventory.open()
        return true
    }
}