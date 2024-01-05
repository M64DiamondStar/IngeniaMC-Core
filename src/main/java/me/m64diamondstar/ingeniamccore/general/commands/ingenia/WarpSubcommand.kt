package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.utils.EnumUtilities
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import me.m64diamondstar.ingeniamccore.warps.WarpManager
import me.m64diamondstar.ingeniamccore.warps.WarpType
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WarpSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {

    override fun execute() {
        if (sender !is Player) {
            sender.sendMessage(Messages.noPlayer())
            return
        }

        when(args[1]){

            "create" -> {
                if (args.size != 4) {
                    sender.sendMessage(Messages.commandUsage("ig warp create <warp> <type>"))
                    return
                }

                val warpManager = WarpManager()
                warpManager.createWarp(
                    args[2],
                    sender.location,
                    if(EnumUtilities.enumContains<WarpType>(args[3])) WarpType.valueOf(args[3]) else WarpType.DEFAULT
                )
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully created the warp ${args[2]}."))
            }

            "setLocation" -> {
                if (args.size != 3) {
                    sender.sendMessage(Messages.commandUsage("ig warp setLocation <warp>"))
                    return
                }

                val warpManager = WarpManager()
                warpManager.setWarpLocation(args[2], sender.location)
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully set the location of the warp ${args[2]}."))
            }

            "setType" -> {
                if (args.size != 4) {
                    sender.sendMessage(Messages.commandUsage("ig warp setType <warp> <type>"))
                    return
                }

                val warpManager = WarpManager()
                warpManager.setWarpType(
                    args[2],
                    if(EnumUtilities.enumContains<WarpType>(args[3])) WarpType.valueOf(args[3]) else WarpType.DEFAULT
                )
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully set the type of the warp ${args[2]}."))
            }

            "setItem" -> {
                if (args.size != 3) {
                    sender.sendMessage(Messages.commandUsage("ig warp setItem <warp>"))
                    return
                }

                val warpManager = WarpManager()
                warpManager.setWarpItem(args[2], sender.inventory.itemInMainHand)
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully set the item of the warp ${args[2]}."))
            }

            "delete" -> {
                if (args.size != 3) {
                    sender.sendMessage(Messages.commandUsage("ig warp delete <warp>"))
                    return
                }

                val warpManager = WarpManager()
                warpManager.deleteWarp(args[2])
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully deleted the warp ${args[2]}."))
            }

            "reload" -> {
                val warpManager = WarpManager()
                warpManager.reload()
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully reloaded the warps."))
            }
        }

    }

    override fun getTabCompleters(): ArrayList<String> {
        val tabs = ArrayList<String>()

        if(args.size == 2){
            tabs.add("create")
            tabs.add("setLocation")
            tabs.add("setType")
            tabs.add("setItem")
            tabs.add("delete")
            tabs.add("reload")
        }

        if(args.size == 3 && !args[1].equals("reload", ignoreCase = true) && !args[1].equals("create", ignoreCase = true)){
            val warpManager = WarpManager()
            tabs.addAll(warpManager.getAllIDs())
        }

        if(args.size == 4 && (args[1].equals("create", ignoreCase = true) || args[1].equals("setType", ignoreCase = true))){
            tabs.addAll(WarpType.values().map { it.name })
        }

        return tabs
    }

}