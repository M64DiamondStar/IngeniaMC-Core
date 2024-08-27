package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.protect.FeatureManager
import me.m64diamondstar.ingeniamccore.protect.FeatureType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SpawnCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage(Messages.noPlayer())
            return false
        }

        val featureManager = FeatureManager()
        if(!featureManager.isFeatureEnabled(FeatureType.WARPS) && !sender.hasPermission("ingenia.admin")){
            sender.sendMessage(Messages.featureDisabled())
            return false
        }
        val ingeniaPlayer = IngeniaPlayer(sender)
        ingeniaPlayer.teleport(IngeniaMC.spawn)

        return false
    }

}