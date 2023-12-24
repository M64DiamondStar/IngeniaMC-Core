package me.m64diamondstar.ingeniamccore.general.commands.ingenia.cosmetic

import me.m64diamondstar.ingeniamccore.cosmetics.data.JoinLeaveColor
import me.m64diamondstar.ingeniamccore.general.player.CosmeticPlayer
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class CosmeticColorSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {

    /**
     * Execute the command
     */
    override fun execute() {
        if (args.size <= 3) {
            sender.sendMessage(Colors.format(Messages.commandUsage("ig cosmeticcolor <add/remove> <player> <id>")))
        }

        else if (args.size == 4) {

            val target = Bukkit.getPlayer(args[2])
            if (target == null) {
                sender.sendMessage(Messages.invalidPlayer())
                return
            }

            val targetPlayer = CosmeticPlayer(target)
            if (args[1].equals("add", ignoreCase = true)) {
                targetPlayer.addMessageColor(args[3])
                sender.sendMessage(
                    Colors.format(
                        MessageType.SUCCESS + "Successfully added message color " + args[3] + " to " + target.name + "."
                    )
                )
            }

            else if (args[1].equals("remove", ignoreCase = true)) {
                targetPlayer.removeMessageColor(args[3])
                sender.sendMessage(
                    Colors.format(
                        MessageType.SUCCESS + "Successfully removed message color " + args[3] + " to " + target.name + "."
                    )
                )
            }

            else {
                sender.sendMessage(
                    Colors.format(
                        Messages.commandUsage("ig cosmetic <add/remove> <player> <cosmetic type> <id>")))
            }
        }
    }


    override fun getTabCompleters(): ArrayList<String>{
        val tabs = ArrayList<String>()

        when (args.size) {
            2 -> {
                tabs.add("add")
                tabs.add("remove")
            }
            3 -> {
                for (player in Bukkit.getOnlinePlayers())
                    tabs.add(player.name)
            }
            4 -> {
                val cosmeticItems = JoinLeaveColor()
                tabs.addAll(cosmeticItems.getAllIDs())

            }
        }

        return tabs
    }
}