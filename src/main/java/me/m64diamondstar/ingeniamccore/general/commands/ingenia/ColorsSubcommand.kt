package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.cosmetics.data.JoinLeaveColor
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ColorsSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {

    override fun execute() {
        if (sender !is Player) {
            sender.sendMessage(Messages.noPlayer())
            return
        }

        if(args.size < 3){
            sender.sendMessage(Messages.commandUsage("ig colors <create/delete/set/get/exists> <ID> [name/color] [color]"))
            return
        }

        try{
            val joinLeaveColor = JoinLeaveColor()
            if(!joinLeaveColor.exists(args[2]) &&
                !(args[1].equals("create", ignoreCase = true) || args[1].equals("exists", ignoreCase = true))){
                sender.sendMessage(Colors.format(MessageType.ERROR + "This ID does not exist."))
                return
            }

            when(args[1].lowercase()){
                "exists" -> {
                    if(joinLeaveColor.exists(args[2])){
                        sender.sendMessage(Colors.format(MessageType.SUCCESS + "This ID exists."))
                    }else
                        sender.sendMessage(Colors.format(MessageType.ERROR + "This ID does not exist."))
                }

                "create" -> {
                    if(args.size == 5) {
                        val name = args[3].replace("-", " ").replace("_", " ")
                        val color = args[4]
                        joinLeaveColor.create(args[2], color, name, false)
                        sender.sendMessage(
                            Colors.format(
                                MessageType.SUCCESS + "Successfully created the color ${args[2]} with the name " +
                                        "$name and the color ${args[4]}test."
                            )
                        )
                    }
                }

                "delete" -> {
                    joinLeaveColor.delete(args[2])
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully deleted the color ${args[2]}."))
                }

                "get" -> {
                    val color = joinLeaveColor.getColor(args[2])
                    val name = joinLeaveColor.getName(args[2])
                    if(color == null){
                        sender.sendMessage(Colors.format(MessageType.ERROR + "The item is null and couldn't be loaded."))
                        return
                    }
                    sender.sendMessage(Colors.format("Color: " +
                            "${joinLeaveColor.getHexColor(args[2])}test test"))
                    sender.sendMessage("name: $name")
                }

                "setcolor" -> {
                    if(args.size == 4) {
                        joinLeaveColor.setColor(args[2], args[3])
                        sender.sendMessage(
                            Colors.format(
                                MessageType.SUCCESS + "Successfully set the color ${args[2]} to the color " +
                                        "${args[3]}."
                            )
                        )
                    }
                }

                "setname" -> {
                    if(args.size == 4) {
                        joinLeaveColor.setName(args[2], args[3])
                        sender.sendMessage(
                            Colors.format(
                                MessageType.SUCCESS + "Successfully set the name of ${args[2]} to " +
                                        "${args[3]}."
                            )
                        )
                    }
                }
            }
        }catch (ex: IllegalArgumentException){
            sender.sendMessage(Colors.format(MessageType.ERROR + "Please enter a valid cosmetic type."))
        }
    }

    override fun getTabCompleters(): ArrayList<String> {
        val tabs = ArrayList<String>()

        if(args.size == 2){
            tabs.add("create")
            tabs.add("delete")
            tabs.add("setcolor")
            tabs.add("setname")
            tabs.add("get")
            tabs.add("exists")
        }

        if(args.size == 3){
            try{
                val colors = JoinLeaveColor()
                tabs.addAll(colors.getAllIDs())
            }catch (_: IllegalArgumentException){}
        }

        return tabs
    }
}