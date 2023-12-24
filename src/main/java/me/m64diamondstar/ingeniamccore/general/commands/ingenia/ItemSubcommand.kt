package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.cosmetics.data.CosmeticItems
import me.m64diamondstar.ingeniamccore.cosmetics.utils.CosmeticType
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ItemSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {

    override fun execute() {
        if (sender !is Player) {
            sender.sendMessage(Messages.noPlayer())
            return
        }

        if(args.size != 4){
            sender.sendMessage(Messages.commandUsage("ig item <create/delete/set/get/exists> <cosmetic type> <ID>"))
            return
        }

        try{
            val cosmeticType = CosmeticType.valueOf(args[2])
            val cosmeticItems = CosmeticItems(cosmeticType)
            if(!cosmeticItems.exists(args[3]) &&
                !(args[1].equals("create", ignoreCase = true) || args[1].equals("exists", ignoreCase = true))){
                sender.sendMessage(Colors.format(MessageType.ERROR + "This ID does not exist."))
                return
            }

            when(args[1].lowercase()){
                "exists" -> {
                    if(cosmeticItems.exists(args[3])){
                        sender.sendMessage(Colors.format(MessageType.SUCCESS + "This ID exists."))
                    }else
                        sender.sendMessage(Colors.format(MessageType.ERROR + "This ID does not exist."))
                }

                "create" -> {
                    cosmeticItems.create(args[3], sender.inventory.itemInMainHand, true)
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully created the cosmetic ${args[3]} as the item " +
                            "${sender.inventory.itemInMainHand.type}."))
                }

                "delete" -> {
                    cosmeticItems.delete(args[3])
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully deleted the cosmetic ${args[3]}."))
                }

                "get" -> {
                    val item = cosmeticItems.getItem(args[3])
                    if(item == null){
                        sender.sendMessage(Colors.format(MessageType.ERROR + "The item is null and couldn't be loaded."))
                        return
                    }
                    sender.inventory.addItem(item)
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully added the cosmetic ${args[3]} to your inventory."))
                }

                "set" -> {
                    cosmeticItems.setItem(args[3], sender.inventory.itemInMainHand)
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully set the cosmetic ${args[3]} to the item " +
                            "${sender.inventory.itemInMainHand.type}."))
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
            tabs.add("set")
            tabs.add("get")
            tabs.add("exists")
        }

        if(args.size == 3){
            CosmeticType.values().forEach { tabs.add(it.toString()) }
        }

        if(args.size == 4){
            try{
                val cosmeticType = CosmeticType.valueOf(args[2])
                val cosmeticItems = CosmeticItems(cosmeticType)
                tabs.addAll(cosmeticItems.getAllIDs())
            }catch (_: IllegalArgumentException){}
        }

        return tabs
    }
}