package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.cosmetics.data.JoinLeaveMessage
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MessagesSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {

    override fun execute() {
        if (sender !is Player) {
            sender.sendMessage(Messages.noPlayer())
            return
        }

        if(args.size < 4){
            sender.sendMessage(Messages.commandUsage("ig messages <create/delete/set/get/exists> <message type> <ID> <name> <message...>"))
            return
        }

        try{
            val messageType = me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.valueOf(args[2])
            val joinLeaveMessage = JoinLeaveMessage(messageType)

            if(!joinLeaveMessage.exists(args[3]) &&
                !(args[1].equals("create", ignoreCase = true) || args[1].equals("exists", ignoreCase = true))){
                sender.sendMessage(Colors.format(MessageType.ERROR + "This ID does not exist."))
                return
            }

            /*
             * Create message
             */
            if(args[1].equals("create", ignoreCase = true) && args.size > 5){
                val builder = StringBuilder()
                for(i in 5 until args.size){
                    builder.append(args[i]).append(" ")
                }
                val name = args[4].replace("-", " ").replace("_", " ")

                joinLeaveMessage.create(args[3], builder.toString(), name, false)
                sender.sendMessage(
                    Colors.format(
                        MessageType.SUCCESS + "Successfully created the message ${args[3]} " +
                                "with the name $name with the message: '$builder'."))
            }

            when(args[1].lowercase()){
                "exists" -> {
                    if(joinLeaveMessage.exists(args[3])){
                        sender.sendMessage(Colors.format(MessageType.SUCCESS + "This ID exists."))
                    }else
                        sender.sendMessage(Colors.format(MessageType.ERROR + "This ID does not exist."))
                }

                "delete" -> {
                    joinLeaveMessage.delete(args[3])
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully deleted the message ${args[3]}."))
                }

                "get" -> {
                    val message = joinLeaveMessage.getMessage(args[3])
                    val name = joinLeaveMessage.getName(args[3])
                    if(message == null){
                        sender.sendMessage(Colors.format(MessageType.ERROR + "The message is null and couldn't be loaded."))
                        return
                    }
                    sender.sendMessage("message: $message")
                    sender.sendMessage("name: $name")
                }

                "setmessage" -> {
                    if(args.size > 5) {
                        val builder = StringBuilder()
                        for (i in 4 until args.size) {
                            builder.append(args[i]).append(" ")
                        }
                        joinLeaveMessage.setMessage(args[3], builder.toString())
                        sender.sendMessage(
                            Colors.format(
                                MessageType.SUCCESS + "Successfully set the message ${args[3]} to: '" +
                                        "$builder'."
                            )
                        )
                    }else{
                        sender.sendMessage(Messages.commandUsage("ig messages setmessage <message type> <ID> <message...>"))
                    }
                }

                "setname" -> {
                    if(args.size == 5) {
                        val name = args[4]
                        joinLeaveMessage.setMessage(args[3], name)
                        sender.sendMessage(
                            Colors.format(
                                MessageType.SUCCESS + "Successfully set the name ${args[3]} to " +
                                        "$name."
                            )
                        )
                    }else{
                        sender.sendMessage(Messages.commandUsage("ig messages setname <message type> <ID> <name>"))
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
            tabs.add("setmessage")
            tabs.add("setname")
            tabs.add("get")
            tabs.add("exists")
        }

        if(args.size == 3){
            me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.values().forEach { tabs.add(it.toString()) }
        }

        if(args.size == 4){
            try{
                val cosmeticType = me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.valueOf(args[2])
                val cosmeticItems = JoinLeaveMessage(cosmeticType)
                tabs.addAll(cosmeticItems.getAllIDs())
            }catch (_: IllegalArgumentException){}
        }

        return tabs
    }
}