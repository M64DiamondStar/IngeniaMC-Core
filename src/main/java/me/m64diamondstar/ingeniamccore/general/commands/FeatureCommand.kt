package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.protect.FeatureManager
import me.m64diamondstar.ingeniamccore.protect.FeatureType
import me.m64diamondstar.ingeniamccore.utils.EnumUtilities
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class FeatureCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if(args.isEmpty()) {
            sender.sendMessage(Messages.commandUsage("feature <enable/disable/info> [feature]"))
            return false
        }

        when(args[0]) {
            "enable" -> {
                if(args[1].isEmpty() || !EnumUtilities.enumContains<FeatureType>(args[1])) {
                    sender.sendMessage(Messages.commandUsage("feature enable <feature>"))
                    return false
                }

                val featureManager = FeatureManager()
                featureManager.enableFeature(FeatureType.valueOf(args[1].uppercase()))
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "The feature ${args[1]} has been enabled."))
            }
            "disable" -> {
                if(args[1].isEmpty() || !EnumUtilities.enumContains<FeatureType>(args[1])) {
                    sender.sendMessage(Messages.commandUsage("feature disable <feature>"))
                    return false
                }

                val featureManager = FeatureManager()
                featureManager.disableFeature(FeatureType.valueOf(args[1].uppercase()))
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "The feature ${args[1]} has been disabled."))
            }
            "info" -> {
                val featureManager = FeatureManager()
                sender.sendMessage(Colors.format(MessageType.BACKGROUND + "------ Features ------"))
                FeatureType.entries.forEach {
                    sender.sendMessage(
                        Colors.format("${it.name}: ${
                            if(featureManager.isFeatureEnabled(it)) 
                                "${MessageType.SUCCESS}Enabled" 
                            else "${MessageType.ERROR}Disabled"}")
                    )
                }
                sender.sendMessage(Colors.format(MessageType.BACKGROUND + "------ -------- ------"))
            }
            else -> {
                sender.sendMessage(Messages.commandUsage("feature <enable/disable/info> [feature]"))
            }
        }

        return false
    }

}