package me.m64diamondstar.ingeniamccore.general.commands

import io.papermc.paper.entity.TeleportFlag
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.protect.FeatureManager
import me.m64diamondstar.ingeniamccore.protect.FeatureType
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import me.m64diamondstar.ingeniamccore.warps.WarpManager
import org.bukkit.Particle
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WarpCommand: CommandExecutor {

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

        if(args.size != 1){
            sender.sendMessage(Messages.commandUsage("warp <warp>"))
            return false
        }

        val warpManager = WarpManager()
        val ingeniaPlayer = IngeniaPlayer(sender)

        if(warpManager.getWarpLocation(args[0]) != null){
            ingeniaPlayer.teleport(warpManager.getWarpLocation(args[0])!!)
            return true
        }else{
            sender.sendMessage(Colors.format(MessageType.ERROR + "The warp ${args[0]} does not exist."))
        }

        return false
    }

}