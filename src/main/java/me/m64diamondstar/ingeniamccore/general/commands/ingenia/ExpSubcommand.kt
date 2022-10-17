package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import java.lang.NumberFormatException

class ExpSubcommand(private val sender: CommandSender, private val args: Array<String>) {
    /**
     * Execute the command
     */
    fun execute() {
        if (args.size <= 2) {
            sender.sendMessage(Colors.format(Messages.commandUsage("ig exp <add/set/subtract/get> <player> [amount]")))
        } else if (args.size == 3) {
            if (args[1].equals("get", ignoreCase = true)) {
                val target = IngeniaPlayer(Bukkit.getPlayer(args[2]))
                if (target.player == null) {
                    sender.sendMessage(Messages.invalidPlayer())
                    return
                }
                sender.sendMessage(
                    Colors.format(
                        MessageType.INFO + target.name + " has " + target.exp + " exp."))
            } else {
                sender.sendMessage(
                    Colors.format(
                        Messages.commandUsage("ig exp <add/set/subtract/get> <player> [amount]")))
            }
        } else if (args.size == 4) {
            val exp: Long
            exp = try {
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
                target.addExp(exp)
                sender.sendMessage(
                    Colors.format(
                        MessageType.SUCCESS + "Successfully added " + exp + " to " + target.name + "."
                    )
                )
            } else if (args[1].equals("set", ignoreCase = true)) {
                target.exp = exp
                sender.sendMessage(
                    Colors.format(
                        MessageType.SUCCESS + "Successfully set " + target.name + "'s exp to " + exp + "."
                    )
                )
            } else if (args[1].equals("subtract", ignoreCase = true)) {
                target.addExp(-exp)
                sender.sendMessage(
                    Colors.format(
                        MessageType.SUCCESS + "Successfully subtracted " + exp + " exp from " + target.name + "."
                    )
                )
            } else {
                sender.sendMessage(
                    Colors.format(
                        Messages.commandUsage("ig exp <add/set/subtract/get> <player> [amount]")))
            }
        }
    }


    fun getTabCompleters(): ArrayList<String>{
        var tabs = ArrayList<String>()

        val firstTab = ArrayList<String>()
        firstTab.add("add")
        firstTab.add("set")
        firstTab.add("subtract")
        firstTab.add("get")

        val secondTab = ArrayList<String>()
        for(player in Bukkit.getOnlinePlayers())
            secondTab.add(player.name)

        val thirdTab = ArrayList<String>()
        thirdTab.add("1")
        thirdTab.add("2")
        thirdTab.add("3")
        thirdTab.add("4")
        thirdTab.add("5")
        thirdTab.add("6")
        thirdTab.add("7")
        thirdTab.add("8")
        thirdTab.add("9")

        if(args.size == 2)
            tabs = firstTab
        else if(args.size == 3)
            tabs = secondTab
        else if(args.size == 4 && !args[1].equals("get", true))
            tabs = thirdTab

        return tabs
    }
}