package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.Colors
import me.m64diamondstar.ingeniamccore.utils.MessageType
import me.m64diamondstar.ingeniamccore.utils.Messages
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import java.lang.NumberFormatException

class BalanceSubcommand(private val sender: CommandSender, private val args: Array<String>) {
    /**
     * Execute the command
     */
    fun execute() {
        if (args.size <= 2) {
            sender.sendMessage(Colors.format(Messages.commandUsage("ig balance <add/set/subtract/get> <player> [amount]")))
        } else if (args.size == 3) {
            if (args[1].equals("get", ignoreCase = true)) {
                val target = IngeniaPlayer(Bukkit.getPlayer(args[2]))
                if (target.player == null) {
                    sender.sendMessage(Messages.invalidPlayer())
                    return
                }
                sender.sendMessage(Colors.format(target.name + " has " + target.bal + ":gs:.", MessageType.INFO))
            } else {
                sender.sendMessage(Colors.format(Messages.commandUsage("ig balance <add/set/subtract/get> <player> [amount]")))
            }
        } else if (args.size == 4) {
            val gs: Long
            gs = try {
                args[3].toLong()
            } catch (e: NumberFormatException) {
                sender.sendMessage(Messages.invalidNumber())
                return
            }
            val target = IngeniaPlayer(Bukkit.getPlayer(args[2]))
            if (target.player == null) {
                sender.sendMessage(Messages.invalidPlayer())
                return
            }
            if (args[1].equals("add", ignoreCase = true)) {
                target.addBal(gs)
                sender.sendMessage(
                    Colors.format(
                        "Successfully added " + gs + ":gs: to " + target.name + ".",
                        MessageType.SUCCESS
                    )
                )
            } else if (args[1].equals("set", ignoreCase = true)) {
                target.bal = gs
                sender.sendMessage(
                    Colors.format(
                        "Successfully set " + target.name + "'s balance to " + gs + ":gs:.",
                        MessageType.SUCCESS
                    )
                )
            } else if (args[1].equals("subtract", ignoreCase = true)) {
                target.addBal(-gs)
                sender.sendMessage(
                    Colors.format(
                        "Successfully subtracted " + gs + ":gs: from " + target.name + ".",
                        MessageType.SUCCESS
                    )
                )
            } else {
                sender.sendMessage(Colors.format(Messages.commandUsage("ig balance <add/set/subtract/get> <player> [amount]")))
            }
        }
    }
}