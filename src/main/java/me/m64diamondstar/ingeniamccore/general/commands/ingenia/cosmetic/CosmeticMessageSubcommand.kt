package me.m64diamondstar.ingeniamccore.general.commands.ingenia.cosmetic

import me.m64diamondstar.ingeniamccore.cosmetics.data.JoinLeaveMessage
import me.m64diamondstar.ingeniamccore.general.player.CosmeticPlayer
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class CosmeticMessageSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {

    /**
     * Execute the command
     */
    override fun execute() {
        if (args.size <= 4) {
            sender.sendMessage(Colors.format(Messages.commandUsage("ig cosmeticmessage <add/remove> <player> <JOIN/LEAVE> <id>")))
        }

        else if (args.size == 5) {
            val type: me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType = try {
                me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.valueOf(args[3].uppercase())
            } catch (e: IllegalArgumentException) {
                sender.sendMessage(Colors.format(MessageType.ERROR + "Invalid cosmetic type."))
                return
            }

            val target = Bukkit.getPlayer(args[2])
            if (target == null) {
                sender.sendMessage(Messages.invalidPlayer())
                return
            }

            val targetPlayer = CosmeticPlayer(target)
            if (args[1].equals("add", ignoreCase = true)) {
                when (type) {
                    me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.JOIN -> targetPlayer.addJoinMessage(args[4])
                    me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.LEAVE -> targetPlayer.addLeaveMessage(args[4])
                }
                sender.sendMessage(
                    Colors.format(
                        MessageType.SUCCESS + "Successfully added ${args[3].lowercase()} message " + args[4] + " to " + target.name + "."
                    )
                )
            }

            else if (args[1].equals("remove", ignoreCase = true)) {
                when (type) {
                    me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.JOIN -> targetPlayer.removeJoinMessage(args[4])
                    me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.LEAVE -> targetPlayer.removeLeaveMessage(args[4])
                }
                sender.sendMessage(
                    Colors.format(
                        MessageType.SUCCESS + "Successfully removed ${args[3].lowercase()} message " + args[4] + " to " + target.name + "."
                    )
                )
            }

            else {
                sender.sendMessage(
                    Colors.format(
                        Messages.commandUsage("ig cosmeticmessage <add/remove> <player> <JOIN/LEAVE> <id>")))
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
                tabs.addAll(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.values().map { it.name })
            }
            5 -> {
                if(!me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.values().map { it.name }.contains(args[3].uppercase())) tabs.add("INVALID_TYPE")
                else{
                    val cosmeticType = me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.valueOf(args[3].uppercase())
                    val cosmeticItems = JoinLeaveMessage(cosmeticType)
                    tabs.addAll(cosmeticItems.getAllIDs())
                }
            }
        }

        return tabs
    }
}