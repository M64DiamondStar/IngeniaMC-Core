package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.protect.FeatureManager
import me.m64diamondstar.ingeniamccore.protect.FeatureType
import me.m64diamondstar.ingeniamccore.ranks.joinleavemessage.JoinLeaveInventory
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class EditJoinLeaveCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, string: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage(Messages.noPlayer())
            return false
        }

        val featureManager = FeatureManager()
        if(!featureManager.isFeatureEnabled(FeatureType.MENU) && !sender.hasPermission("ingenia.admin")){
            sender.sendMessage(Messages.featureDisabled())
            return false
        }

        if(!sender.hasPermission("ingenia.vip")){
            sender.sendMessage(Messages.requiresVIP())
            return false
        }

        if(string.equals("editjoinmessage", ignoreCase = true)){
            val joinLeaveInventory = JoinLeaveInventory(sender, JoinLeaveInventory.Selected.JOIN)
            joinLeaveInventory.open()
        }else{
            val joinLeaveInventory = JoinLeaveInventory(sender, JoinLeaveInventory.Selected.LEAVE)
            joinLeaveInventory.open()
        }

        return false
    }
}